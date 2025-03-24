package com.project.template.common.cache

import com.alibaba.fastjson2.JSON
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils

abstract class CacheTemplate {

    /**
     * 存储一个值
     * @param [key] 钥匙
     * @param [value] 价值
     */
    abstract fun put(key: String, value: Any)

    /**
     * 获取value，如果不存在则为null
     * @param [key] 钥匙
     * @return [Any?]
     */
    abstract operator fun get(key: String): Any?

    /**
     * 获取并转换为指定对象
     * @param [key] 钥匙
     * @param [clazz] 克拉兹
     * @return [T?]
     */
    fun <T> getParse(key: String, clazz: Class<T>): T? {
        val value = get(key)
        if (ObjectUtils.isEmpty(value) || StringUtils.isBlank(value.toString())) {
            return null
        }
        return JSON.parseObject(value.toString(), clazz)
    }

    abstract fun getIfPresent(key: String): Any?

    fun <T> getIfPresentParse(key: String, clazz: Class<T>): T? {
        val value = getIfPresent(key)
        if (ObjectUtils.isEmpty(value) || StringUtils.isBlank(value.toString())) {
            return null
        }
        return JSON.parseObject(value.toString(), clazz)
    }

    abstract fun remove(key: String)

    abstract fun remove(keys:Iterable<String>)

    abstract fun removeAll()

}