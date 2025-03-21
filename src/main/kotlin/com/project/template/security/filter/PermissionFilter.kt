package com.project.template.security.filter

import com.project.template.security.exception.enum.AuthExceptionEnum
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
    /**
     * 不需要过滤的路由地址
     */
    @Value("\${spring.security.allow.uri}")
    private val uri: ArrayList<String>
) : OncePerRequestFilter() {

    private val matcher = AntPathMatcher()


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
        val token = request.getHeader("Authorization")
        // 2.1、如果token不存在，触发异常
        if (StringUtils.isBlank(token)) {
            throw BadCredentialsException(
                String.format(
                    "[%s]-%s",
                    AuthExceptionEnum.NOT_LOGIN.code,
                    AuthExceptionEnum.NOT_LOGIN.message
                )
            )
        }
        // 验证token
    }

}