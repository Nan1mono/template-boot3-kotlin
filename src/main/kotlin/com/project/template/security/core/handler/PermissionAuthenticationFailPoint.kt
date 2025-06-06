package com.project.template.security.core.handler

import com.alibaba.fastjson2.JSON
import com.project.template.com.project.template.common.result.Result2
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

/**
 * 权限认证失败处理器
 * 这里将返回认证失败的信息
 */
class PermissionAuthenticationFailPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        //转换成json字符串
        val result = JSON.toJSONString(Result2.authFail(authException.localizedMessage))
        //返回响应
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED;
        response.writer.println(result)
    }
}