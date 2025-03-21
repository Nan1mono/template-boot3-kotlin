package com.project.template.com.project.template.common.log.annotation

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Slf4j2 {
    companion object {
        val <reified T> T.log: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }
}
