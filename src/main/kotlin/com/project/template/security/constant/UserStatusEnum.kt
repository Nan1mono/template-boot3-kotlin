package com.project.template.security.constant

enum class UserStatusEnum(
    val code: Int,
    val description: String
) {
    DELETED(1, "已删除"),
    UNDELETED(0, "未删除"),
    NORMAL(0, "正常"),
    LOCKED(1, "锁定"),
    ENABLED(1, "启用"),
    DISABLED(0, "禁用");
}