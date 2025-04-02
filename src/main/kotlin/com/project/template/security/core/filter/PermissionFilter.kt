package com.project.template.security.core.filter

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
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class PermissionFilter(
    @Value("\${template.token.sign-key:nan1mono}")
    private val sign: String,

    private val redisUtils: RedisUtils
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
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
            throw BadCredentialsException(AuthFailEnum.TOKEN_EXPIRED.buildMessage())
        }
        // 判断账号是否锁定，是否启用
        val auth = AuthSuccessResponse.buildWithUsernameToken(token)
        val cacheAuth = AuthSuccessResponse.buildWithUsernameToken(cacheToken)
        if (!auth.isNonLocked || !cacheAuth.isNonLocked) {
            throw BadCredentialsException(AuthFailEnum.USER_LOCKED.buildMessage())
        }
        if (!auth.isEnabled || !cacheAuth.isEnabled) {
            throw BadCredentialsException(AuthFailEnum.USER_DISABLED_OR_DELETED.buildMessage())
        }
        // 认证通过，添加上下文西信息
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(token, token, auth.getSimpleGrantedAuthorities())
        doFilter(request, response, filterChain)
    }
}