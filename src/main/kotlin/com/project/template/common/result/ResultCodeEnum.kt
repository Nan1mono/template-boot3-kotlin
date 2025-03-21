package com.project.template.com.project.template.common.result

enum class ResultCodeEnum(
    val code: Int,
    val message: String
) {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    UNKNOWN_ERROR(500, "服务发生异常"),
}