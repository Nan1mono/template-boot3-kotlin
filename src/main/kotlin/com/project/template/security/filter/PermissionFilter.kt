package com.project.template.security.filter

import com.project.template.common.cache.CacheManager
import com.project.template.security.entity.SecurityUserDetail
import com.project.template.security.exception.enum.AuthFailEnum
import com.project.template.security.utils.JwtHelper
import io.swagger.v3.oas.models.PathItem.HttpMethod
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

/**
 * security过滤器链
 * 包含了一系列登录与权限的校验
 * TODO date: 2025年3月21日    description: 说明待补充
 */
@Component
open class PermissionFilter(

    @Value("\${template.token.expiration:25200}")
    private val uri: ArrayList<String>,
    @Value("\${template.token.sign-key:nan1mono}")
    private val sign: String,
    cacheManager: CacheManager
) : OncePerRequestFilter() {

    private val matcher = AntPathMatcher()

    private val cacheTemplate = cacheManager.createTemplate()

    /**
     * 核心过滤方法
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1、路由白名单过滤：1、如果是路由白名单，跳过；2、如果是OPTION预请求，跳过
        if (request.method == HttpMethod.OPTIONS.name || uri.stream()
                .anyMatch { t -> matcher.match(t, request.requestURI) }
        ) {
            filterChain.doFilter(request, response)
            return
        }
        // 2、token校验，判断token是否正确，是否过期
        val token = request.getHeader("Authorization").replace("Bearer ", "")
        // 2.1、如果token不存在，触发异常
        if (StringUtils.isBlank(token)) {
            throw BadCredentialsException(AuthFailEnum.NOT_LOGIN.buildMessage())
        }
        // 2.2、校验token：发行人+签名+过期时间
        if (!JwtHelper.verify(token, sign)) {
            throw BadCredentialsException(AuthFailEnum.TOKEN_VERIFY_FAIL.buildMessage())
        }
        // 2.3、通过token获取userId，并从缓存中获取用户信息
        val userId = JwtHelper.getUserId(token)
        val userDetail = (cacheTemplate[userId.toString()]
            ?: throw BadCredentialsException(AuthFailEnum.NOT_LOGIN.buildMessage())).run { this as SecurityUserDetail }
        // 验证用户是否被锁定
        if (!userDetail.isAccountNonLocked){
            // TODO date: 2025年3月24日    description: 处理账户被锁定时的异常
        }
    }

}