package com.project.template.com.project.template.common.result

enum class ResultCodeEnum(
    val code: Int,
    val message: String
) {
    SUCCESS(200, "success"),
    FAIL(201, "fail"),
    UNKNOWN_ERROR(500, "UNKNOWN_ERROR"),
}