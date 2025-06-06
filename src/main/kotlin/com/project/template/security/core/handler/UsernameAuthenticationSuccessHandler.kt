package com.project.template.security.core.handler

import com.alibaba.fastjson2.JSON
import com.project.template.com.project.template.common.result.Result2
import com.project.template.security.core.entity.AuthSuccessResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler


/**
 * 登录成功处理器
 * 这里将控制登录成功之后的返回
 */
class UsernameAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // 校验结果
        val token = authentication.principal.toString()
        //创建结果对象
        val successResponse = AuthSuccessResponse.buildWithUsernameToken(token)
        //转换成json字符串
        val result = JSON.toJSONString(Result2.success(successResponse))
        //返回响应
        response.contentType = "application/json;charset=UTF-8"
        response.writer.println(result)
    }
}