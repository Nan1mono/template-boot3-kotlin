package com.project.template.common.cache.impl

import com.project.template.common.cache.CacheTemplate
import com.project.template.common.cache.core.LocalCacheHelper

class LocalCacheTemplate: CacheTemplate() {

    override fun put(key: String, value: Any) {
        LocalCacheHelper.put(key, value)
    }

    override fun get(key: String): Any {
        return LocalCacheHelper[key]
    }

    override fun getIfPresent(key: String): Any? {
        return LocalCacheHelper.getIfPresent(key)
    }

    override fun remove(key: String) {
        LocalCacheHelper.remove(key)
    }

    override fun remove(keys: Iterable<String>) {
        LocalCacheHelper.remove(keys)
    }

    override fun removeAll() {
        LocalCacheHelper.removeAll()
    }
}