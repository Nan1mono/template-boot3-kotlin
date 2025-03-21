package com.project.template.security.exception.enum

enum class AuthExceptionEnum(val number:Int, val code:String, val message:String) {
    USER_PASSWORD_EMPTY(3001, "sec-3001", "Security校验异常：密码为空"),
    USER_USERNAME_EMPTY(3002, "sec-3002", "Security校验异常：用户名为空"),
    NOT_LOGIN(3003, "sec-3003", "Security校验异常：用户未登录"),
}