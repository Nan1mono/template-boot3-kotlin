package com.project.template.security.exception.enum

enum class AuthFailEnum(val number:Int, val code:String, val message:String) {
    USER_PASSWORD_EMPTY(3001, "sec-3001", "Security校验异常：密码为空"),
    USER_USERNAME_EMPTY(3002, "sec-3002", "Security校验异常：用户名为空"),
    NOT_LOGIN(3003, "sec-3003", "Security校验异常：用户未登录"),
    TOKEN_EXPIRED(30004, "sec-30004", "Security校验异常：Token已过期"),
    TOKEN_VERIFY_FAIL(30005, "sec-30005", "Security校验异常：Token校验失败"),
    TOKEN_DECODE_FAIL(30006, "sec-30006", "Security校验异常：Token解密失败")
}