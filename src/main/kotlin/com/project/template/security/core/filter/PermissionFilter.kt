package com.project.template.security.core.filter

import com.alibaba.fastjson2.util.DateUtils
import com.project.template.common.cache.RedisUtils
import com.project.template.security.core.entity.AuthSuccessResponse
import com.project.template.security.exception.enum.AuthFailEnum
import com.project.template.security.utils.JwtHelper
import com.project.template.security.utils.SecurityUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime

/**
 * 请求认证处理器
 * 这里会处理白名单和登录登出以外的请求认证
 * 认证不通过的都将会被定向到PermissionAuthenticationFailPoint
 */
@Component
class PermissionFilter(
    @Value("\${template.token.sign-key:nan1mono}")
    private val sign: String,
    @Value("\${template.security.allow.uri}")
    private val uri: ArrayList<String>,

    private val redisUtils: RedisUtils
) : OncePerRequestFilter() {

    private val matcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 路由白名单过滤
        if (request.method == HttpMethod.OPTIONS.name() || uri.stream()
                .anyMatch { matcher.match(it, request.requestURI) }
        ) {
            filterChain.doFilter(request, response)
            return
        }
        var token = request.getHeader("Authorization")
        // 验证token是否存在
        if (StringUtils.isBlank(token)) {
            throw BadCredentialsException(AuthFailEnum.NOT_LOGIN.buildMessage())
        }
        token = token.replace("Bearer ", "")
        // 获取token中的userId
        val cacheToken = redisUtils[SecurityUtils.buildUserCacheKey(JwtHelper.getUserId(token))]?.toString()
        // 判断token是否相同并验签，需要对token和cacheToken同时验签，确保同时生效
        if (StringUtils.isBlank(cacheToken)
            || cacheToken != token
            || !JwtHelper.verify(token, sign)
            || !JwtHelper.verify(cacheToken, sign)
        ) {
            // 清空redis
            redisUtils.del(SecurityUtils.buildUserCacheKey(JwtHelper.getUserId(token)))
            throw BadCredentialsException(AuthFailEnum.TOKEN_EXPIRED.buildMessage())
        }
        // 判断账号是否锁定，是否启用
        val auth = AuthSuccessResponse.buildWithUsernameToken(token)
        val cacheAuth = AuthSuccessResponse.buildWithUsernameToken(cacheToken)
        val nowDateTime = LocalDateTime.now()
        val authLockDateTime = DateUtils.parseLocalDateTime(auth.lockDateTime)
        val cacheAuthLockDateTime = DateUtils.parseLocalDateTime(cacheAuth.lockDateTime)
        if ((!auth.isNonLocked && authLockDateTime != null && nowDateTime.isBefore(authLockDateTime))
            || (!cacheAuth.isNonLocked && cacheAuthLockDateTime != null && nowDateTime.isBefore(cacheAuthLockDateTime))
        ) {
            redisUtils.del(SecurityUtils.buildUserCacheKey(JwtHelper.getUserId(token)))
            throw BadCredentialsException(AuthFailEnum.USER_LOCKED.buildMessage())
        }
        if (!auth.isEnabled || !cacheAuth.isEnabled) {
            redisUtils.del(SecurityUtils.buildUserCacheKey(JwtHelper.getUserId(token)))
            throw BadCredentialsException(AuthFailEnum.USER_DISABLED_OR_DELETED.buildMessage())
        }
        // 校验tokenIp，缓存Ip，和请求Ip是否一直，如果不一致为异常
        if (auth.remoteIp != cacheAuth.remoteIp || auth.remoteIp != request.remoteAddr || cacheAuth.remoteIp != request.remoteAddr) {
            redisUtils.del(SecurityUtils.buildUserCacheKey(JwtHelper.getUserId(token)))
            throw BadCredentialsException(AuthFailEnum.AUTH_ENVIRONMENT_ERROR.buildMessage())
        }
        // 认证通过，添加上下文西信息
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(token, token, auth.getSimpleGrantedAuthorities())
        doFilter(request, response, filterChain)
    }
}