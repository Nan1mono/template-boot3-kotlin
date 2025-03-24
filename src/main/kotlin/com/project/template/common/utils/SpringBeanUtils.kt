package com.project.template.common.utils

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
open class SpringBeanUtils(private val context: ApplicationContext) {
    fun <E> getBean(clazz: Class<E>): E? {
        return context.getBean(clazz)
    }

    fun getBean(name: String): Any? {
        return context.getBean(name)
    }
}