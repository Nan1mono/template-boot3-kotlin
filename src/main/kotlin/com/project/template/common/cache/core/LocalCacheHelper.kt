package com.project.template.common.cache.core

import com.alibaba.fastjson2.JSON
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.project.template.com.project.template.common.log.annotation.Slf4j2
import com.project.template.com.project.template.common.log.annotation.Slf4j2.Companion.log
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import java.util.concurrent.TimeUnit

/**
 * 本次缓存控制器
 * 用于操纵本地缓存
 */
@Slf4j2
class LocalCacheHelper {

    companion object {
        private val cache: Cache<String, Any> = CacheBuilder.newBuilder()
            // 写缓存最大并发数
            .concurrencyLevel(0)
            // 过期时间
            .expireAfterWrite(7, TimeUnit.DAYS)
            .initialCapacity(10)
            .maximumSize(100)
            .recordStats()
            .removalListener<String, Any> { log.warn("local cache: {} is removed, reason: {}", it.key, it.cause) }
            .build()


        /**
         * 存储值
         * @param [key] 钥匙
         * @param [value] 价值
         */
        fun put(key: String, value: Any) {
            cache.put(key, value)
        }

        /**
         * 获取一个值，如果不存在，转换为空串
         * @param [key] 钥匙
         * @return [Any]
         */
        operator fun get(key: String): Any {
            return cache[key, { "" }]
        }

        /**
         * 获取解析
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

        /**
         * 如果key对应的value存在，获取该值，否则转换为null
         * @param [key] 钥匙
         * @return [Any?]
         */
        fun getIfPresent(key: String): Any? {
            return cache.getIfPresent(key)
        }

        /**
         * 如果key对应的value存在，获取该值，并转换为指定的对象，否则返回null
         * @param [key] 钥匙
         * @param [clazz] 克拉兹
         * @return [T?]
         */
        fun <T> getIfPresentParse(key:String, clazz: Class<T>): T? {
            val value = cache.getIfPresent(key)
            if (ObjectUtils.isEmpty(value) || StringUtils.isBlank(value.toString())){
                return null
            }
            return JSON.parseObject(value.toString(), clazz)
        }

        fun remove(key: String) {
            cache.invalidate(key)
        }

        fun remove(keys:Iterable<String>) {
            cache.invalidateAll(keys)
        }

        fun removeAll() {
            cache.invalidateAll()
        }
    }

}