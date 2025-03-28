package com.project.template.com.project.template.common.exception

import com.project.template.com.project.template.common.log.annotation.Slf4j2
import com.project.template.com.project.template.common.log.annotation.Slf4j2.Companion.log
import com.project.template.com.project.template.common.result.Result2
import com.project.template.security.exception.AuthException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Slf4j2
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AuthException::class)
    fun handlerAuthException(e: AuthException): Result2<Any> {
        log.error(e.stackTraceToString())
        return Result2.fail(e.localizedMessage)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): Result2<Any> {
        log.error(e.stackTraceToString())
        return Result2.fail(e.localizedMessage)
    }

}