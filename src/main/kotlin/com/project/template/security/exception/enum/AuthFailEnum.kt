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
    PASSWORD_EXPIRED(30010, "sec-30010", "密码已过期"),
    USER_DISABLED_OR_DELETED(30011, "sec-30011", "用户被禁用或已删除"),
    USERNAME_OR_PASSWORD_EMPTY(30012, "sec-30012", "用户名或密码为空"),
    AUTH_FAIL(30013, "sec-30013", "认证失败"),
    AUTH_ENVIRONMENT_ERROR(30014, "sec-30014", "认证环境异常"),
    ;
}