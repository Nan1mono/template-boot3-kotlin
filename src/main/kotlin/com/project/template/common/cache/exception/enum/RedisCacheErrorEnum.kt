package com.project.template.common.cache.exception.enum

import com.project.template.common.exception.enum.BaseFailEnum

enum class RedisCacheErrorEnum(
    override val number: Int, override val code: String, override val message: String
) : BaseFailEnum {
    REDIS_DECR_FAIL(4001, "redis-4001", "redis decrement fail"),
}