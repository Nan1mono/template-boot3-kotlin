package com.project.template.common.exception.enum

interface BaseFailEnum {
    val number: Int
    val code: String
    val message: String

    fun buildMessage(): String {
        return "[${this.code}]：${message}"
    }
}

