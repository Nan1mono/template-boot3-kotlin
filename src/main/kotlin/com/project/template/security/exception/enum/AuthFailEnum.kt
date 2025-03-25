package com.project.template.security.exception.enum

import com.project.template.common.exception.enum.BaseFailEnum

enum class AuthFailEnum(override val number: Int, override val code: String, override val message: String) :
    BaseFailEnum {
    USER_PASSWORD_EMPTY(3001, "sec-3001", "密码为空"),
    USER_USERNAME_EMPTY(3002, "sec-3002", "用户名为空"),
    NOT_LOGIN(3003, "sec-3003", "用户未登录"),
    TOKEN_EXPIRED(30004, "sec-30004", "Token已过期"),
    TOKEN_VERIFY_FAIL(30005, "sec-30005", "Token校验失败"),
    TOKEN_DECODE_FAIL(30006, "sec-30006", "Token解密失败"),
    USER_NOT_EXIST(30007, "sec-30007", "用户不存在"),
    PASSWORD_ERROR(3008, "sec-3008", "账号或密码错误"),
    USER_LOCKED(3009, "sec-3009", "用户被锁定"),
    ;
}