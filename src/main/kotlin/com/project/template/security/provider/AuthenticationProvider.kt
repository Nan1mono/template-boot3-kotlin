package com.project.template.security.provider

import com.project.template.common.cache.CacheManager
import com.project.template.module.system.entity.User
import com.project.template.module.system.service.UserService
import com.project.template.security.constant.UserStatusEnum
import com.project.template.security.entity.SecurityUserDetail
import com.project.template.security.exception.AuthException
import com.project.template.security.exception.enum.AuthFailEnum
import com.project.template.security.utils.JwtHelper
import com.project.template.security.utils.SecurityUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * 核心登录校验与处理器
 * 在这里处理登录的校验，token生成，上下文注册等
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
    private val cacheTemplateManager: CacheManager,
    private val userRoleService: UserDetailsService
) : AbstractUserDetailsAuthenticationProvider() {

    companion object {
        private const val LOCKED_KEY = "user:error-pass:error-num:"
    }

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails?,
        authentication: UsernamePasswordAuthenticationToken?
    ) {
        TODO("Not yet implemented")
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
        val cacheTemplate = cacheTemplateManager.createTemplate()
        // 校验密码，因为登陆时获取的是暗文密码，所以比对时需要将密码加密后比对
        val presentPassword = authentication.credentials.toString()
        val username = authentication.name
        // 数据库查询获取用户信息
        var securityUserDetail = this.retrieveUser(username, authentication as UsernamePasswordAuthenticationToken)
        // 开始匹配密码（加密后）
        if (!SecurityUtils.matchPassword(securityUserDetail.password, presentPassword)) {
            // 如果密码不匹配，将开始进行计数，当达到最大锁定次数时，账号锁定
            this.countPasswordErrorTime(securityUserDetail.user)
            throw AuthException(AuthFailEnum.PASSWORD_ERROR)
        }
        // 校验通过，开始签发token
        securityUserDetail = securityUserDetail.apply {
            token = securityUserDetail.user.run {
                JwtHelper.createToken(
                    this.id!!,
                    this.username!!,
                    this.nickname,
                    this.realName,
                    tokenExpiration,
                    tokenSignKey
                )
            }
        }
        // 生成权限信息体
        val authenticated = UsernamePasswordAuthenticationToken.authenticated(
            securityUserDetail,
            securityUserDetail.password,
            securityUserDetail.authorities
        )
        // 处于安全考虑将用户密码设置为空
        securityUserDetail.user.password = null
        // 查询缓存是否存在，如果缓存存在，会刷新过期时间
        val cacheUserDetail = cacheTemplate.getParse(SecurityUtils.buildUserCacheKey(securityUserDetail.user.id), SecurityUserDetail::class.java)
        // 如果缓存不存在，则将数据放到缓存中
        if (cacheUserDetail == null){
            cacheTemplate.put(SecurityUtils.buildUserCacheKey(securityUserDetail.user.id), securityUserDetail)
        }
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
        if (user.isLocked == UserStatusEnum.LOCKED.code) {
            throw AuthException(AuthFailEnum.USER_LOCKED)
        }
        // 如果未被锁定，开始计算错误次数，错误次数存放在缓存中
        val localKey = LOCKED_KEY + user.id
        val cacheTemplate = cacheTemplateManager.createTemplate()
        // 缓存中获取错误次数，如果为空，则初始化为0
        var lockCount = cacheTemplate.getIfPresent(localKey) as? Int ?: 0
        // 当错误次数小于未达到设定好的错误次数时，则继续增加错误次数
        if (lockCount < times) {
            lockCount += 1
            cacheTemplate.put(localKey, lockCount)
            return;
        }
        // 如果已经达到上限，清楚所有缓存数据，并锁定账号信息
        userService.ktUpdate().eq(User::id, user.id).apply {
            this[User::isLocked] = UserStatusEnum.LOCKED.code
            this[User::lockDatetime] = LocalDateTime.now()
        }.update()
        // 清楚缓存数据和context上下文数据
        cacheTemplate.remove(SecurityUtils.buildUserCacheKey(user.id))
        SecurityUtils.clearUser()
    }


    /**
     * 查询该用户对应的角色权限
     */
    fun findUserRole(securityUserDetail: SecurityUserDetail){

    }
}