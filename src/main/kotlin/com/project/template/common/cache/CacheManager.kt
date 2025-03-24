package com.project.template.common.cache

import com.project.template.common.cache.impl.LocalCacheTemplate
import com.project.template.common.utils.SpringBeanUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("cacheManager")
open class CacheManager(
    private val springBeanUtils: SpringBeanUtils
) {

    fun createTemplate(): CacheTemplate {
        return LocalCacheTemplate()
    }

}