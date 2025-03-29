package com.project.template.common.cache.exception

import com.project.template.common.cache.exception.enum.RedisCacheErrorEnum

class RedisCacheException(message:String):RuntimeException(message) {
    constructor(enum: RedisCacheErrorEnum):this(enum.buildMessage())
}