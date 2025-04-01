package com.project.template.security.core.handler

import com.alibaba.fastjson2.JSON
import com.project.template.com.project.template.common.result.Result2
import com.project.template.security.core.entity.AuthSuccessResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler


class UsernameAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // 校验结果
        val token = authentication.principal.toString()
        //创建结果对象
        val successResponse = AuthSuccessResponse.build(token)
        //转换成json字符串
        val result = JSON.toJSONString(Result2.success(successResponse))
        //返回响应
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.println(result)
    }
}