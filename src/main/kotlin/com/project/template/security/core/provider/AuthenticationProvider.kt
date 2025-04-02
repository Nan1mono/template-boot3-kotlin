package com.project.template.security.core.provider

import com.project.template.common.cache.RedisUtils
import com.project.template.module.system.entity.User
import com.project.template.module.system.service.UserService
import com.project.template.security.constant.UserStatusEnum
import com.project.template.security.core.entity.SecurityUserDetail
import com.project.template.security.exception.AuthException
import com.project.template.security.exception.enum.AuthFailEnum
import com.project.template.security.service.SecurityDataService
import com.project.template.security.utils.JwtHelper
import com.project.template.security.utils.SecurityUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime

/**
 * 核心登录校验与处理器
 * 在这里处理登录的校验，token生成，上下文注册等
 *
 * tips：spring security的认证管理器在没有显示指定的provider时，
 * 会扫描所有AuthenticationProvider的类，并添加到manager中，使其注册成为认证者
 */
@Component
open class AuthenticationProvider(
    @Value("\${template.token.expiration:25200}")
    private val tokenExpiration: Long,

    @Value("\${template.token.sign-key:nan1mono}")
    private val tokenSignKey: String,

    @Value("\${template.security.button-enable:true}")
    private val isFind: Boolean,

    @Value("\${template.pass-error.enable:false}")
    private val isCheckLock: Boolean,

    @Value("\${template.pass-error.times:5}")
    private val times: Int,

    @Value("\${template.pass-error.lock-minute:15}")
    private val minute: Long,

    private val userService: UserService,
    private val userDetailsService: UserDetailsService,
    private val redisUtils: RedisUtils,
    private val securityDataService: SecurityDataService
) : AbstractUserDetailsAuthenticationProvider() {

    companion object {
        private const val LOCKED_KEY = "template:auth:user:error-pass:error-num:"
    }

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails, authentication: UsernamePasswordAuthenticationToken
    ) {
        val securityUserDetail = userDetails as SecurityUserDetail
        // 判断用户是否被删除或者未启用
        if (securityUserDetail.user.isDeleted == UserStatusEnum.DELETED.code
            || securityUserDetail.user.status == UserStatusEnum.DISABLED.code
        ) {
            throw AuthException(AuthFailEnum.USER_DISABLED_OR_DELETED)
        }
        // 判断账号是否已经过期
        if (securityUserDetail.user.pwdExpirationTime != null && LocalDateTime.now()
                .isAfter(securityUserDetail.user.pwdExpirationTime)
        ) {
            throw AuthException(AuthFailEnum.PASSWORD_EXPIRED)
        }
        // 判断账号是否被锁定
        val locked = securityUserDetail.user.isLocked
        val lockDatetime = securityUserDetail.user.lockDatetime
        if (locked == UserStatusEnum.LOCKED.code && lockDatetime != null && LocalDateTime.now()
                .isBefore(lockDatetime)
        ) {
            throw AuthException(AuthFailEnum.USER_LOCKED)
        }
    }

    /**
     * 通过用户名获取用户
     */
    override fun retrieveUser(
        username: String,
        authentication: UsernamePasswordAuthenticationToken
    ): SecurityUserDetail {
        return userDetailsService.loadUserByUsername(username) as SecurityUserDetail
    }

    /**
     * 核心校验方法，是本类的主逻辑
     * 校验账号密码，状态等
     */
    override fun authenticate(authentication: Authentication): Authentication {
        // 获取请求ip
        val request = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        // 校验密码，因为登陆时获取的是暗文密码，所以比对时需要将密码加密后比对
        val presentPassword = authentication.credentials.toString()
        val username = authentication.name
        // 数据库查询获取用户信息
        val securityUserDetail = this.retrieveUser(username, authentication as UsernamePasswordAuthenticationToken)
        securityUserDetail.ip = request.request.remoteAddr
        // 开始匹配密码（加密后）
        if (!SecurityUtils.matchPassword(presentPassword, securityUserDetail.password)) {
            // 如果密码不匹配，将开始进行计数，当达到最大锁定次数时，账号锁定
            this.countPasswordErrorTime(securityUserDetail.user)
            throw AuthException(AuthFailEnum.PASSWORD_ERROR)
        }
        // 进行状态校验
        this.additionalAuthenticationChecks(securityUserDetail, authentication)
        // 获取用户角色信息
        securityDataService.buildPermission(securityUserDetail)
        // 处于安全考虑将用户密码设置为空
        securityUserDetail.cleanPassword()
        // 查询缓存是否存在，如果缓存存在，会刷新过期时间，这里是用于确保token的唯一性
        val cacheToken = redisUtils[SecurityUtils.buildUserCacheKey(securityUserDetail.user.id)]
        // 如果缓存不存在，则将token放到缓存中
        if (cacheToken == null || StringUtils.isBlank(cacheToken.toString())) {
            // 校验通过，开始签发token
            securityUserDetail.token = JwtHelper.createToken(securityUserDetail, tokenExpiration, tokenSignKey)
            redisUtils[SecurityUtils.buildUserCacheKey(securityUserDetail.user.id), securityUserDetail.token] =
                tokenExpiration
        } else {
            // 如果缓存存在，不再重新签发token，而是将token重新设置过期时间
            securityUserDetail.token = cacheToken.toString()
            redisUtils.expire(SecurityUtils.buildUserCacheKey(securityUserDetail.user.id), tokenExpiration)
        }
        // 生成权限信息体
        val authenticated = UsernamePasswordAuthenticationToken.authenticated(
            securityUserDetail.token,
            securityUserDetail.token,
            securityUserDetail.authorities
        )
        // 清除redisLock
        redisUtils.del(LOCKED_KEY + securityUserDetail.user.id)
        return authenticated
    }

    /**
     * 计算登录密码错误次数
     * @param [user] 用户信息
     */
    private fun countPasswordErrorTime(user: User) {
        if (!isCheckLock) {
            return;
        }
        // 判断账号是否已经被锁定，如果已经锁定，不再校验
        if (user.isLocked == UserStatusEnum.LOCKED.code
            && user.lockDatetime != null
            && user.lockDatetime!!.isBefore(LocalDateTime.now())
        ) {
            throw AuthException(AuthFailEnum.USER_LOCKED)
        }
        // 如果未被锁定，开始计算错误次数，错误次数存放在缓存中
        val localKey = LOCKED_KEY + user.id
        // 缓存中获取错误次数，如果为空，则初始化为0
        var lockCount = redisUtils[localKey]?.toString()?.toIntOrNull() ?: 0
        // 当错误次数小于未达到设定好的错误次数时，则继续增加错误次数
        if (lockCount < times) {
            lockCount += 1
            redisUtils[localKey] = intArrayOf(lockCount, 60 * 5)
            return
        }
        // 如果已经达到上限，清楚所有缓存数据，并锁定账号信息
        userService.ktUpdate().eq(User::id, user.id).apply {
            this[User::isLocked] = UserStatusEnum.LOCKED.code
            this[User::lockDatetime] = LocalDateTime.now().plusMinutes(minute)
        }.update()
        // 清楚缓存数据和context上下文数据
        redisUtils.del(SecurityUtils.buildUserCacheKey(user.id))
        SecurityUtils.clearUser()
    }
}