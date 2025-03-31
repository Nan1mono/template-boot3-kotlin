package com.project.template.security.core.handler

import com.alibaba.fastjson2.JSON
import com.google.common.collect.Maps
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler


class UsernameAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        //创建结果对象
        val result = Maps.newHashMap<String, Any>()
        result["code"] = 0
        result["message"] = "登录成功"
        //转换成json字符串
        val json = JSON.toJSONString(result)
        //返回响应
        response.contentType = "application/json;charset=UTF-8"
        response.writer.println(json)
    }
}