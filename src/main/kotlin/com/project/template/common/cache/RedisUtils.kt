package com.project.template.common.cache

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.project.template.com.project.template.common.log.annotation.Slf4j2
import com.project.template.com.project.template.common.log.annotation.Slf4j2.Companion.log
import com.project.template.common.cache.exception.RedisCacheException
import com.project.template.common.cache.exception.enum.RedisCacheErrorEnum
import org.apache.commons.lang3.StringUtils
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

@Component
@Slf4j2
class RedisUtils(
    private val redisTemplate: RedisTemplate<Any, Any>
) {

    /**
     * **************** common start ****************
     * /
     *
     *
     * / **
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    fun expire(key: String, time: Long) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS)
            }
        } catch (e: Exception) {
            log.error(e.localizedMessage)
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    fun getExpire(key: String): Long {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS)
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    fun hasKey(key: String): Boolean {
        try {
            val aBoolean = redisTemplate.hasKey(key)
            return true && aBoolean
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    fun del(vararg key: String?) {
        if (key != null && key.size > 0) {
            if (key.size == 1) {
                redisTemplate.delete(key[0])
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key) as Collection<String?>)
            }
        }
    }

    /**
     * **************** common end ****************
     * /
     *
     *
     *
     *
     * / **
     * **************** String start ****************
     * /
     *
     *
     * / **
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    fun get(key: String?): Any? {
        return if (key == null || StringUtils.isBlank(key)) {
            null
        } else {
            redisTemplate.opsForValue()[key]
        }
    }

    fun <T> get(key: String?, clazz: Class<T>): T? {
        val obj = get(key)
        return if (obj == null) null else JSON.parseObject(obj.toString(), clazz)
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    fun set(key: String, value: Any): Boolean {
        try {
            redisTemplate.opsForValue()[key] = value
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    fun set(key: String, value: Any, time: Long): Boolean {
        try {
            if (time > 0) {
                redisTemplate.opsForValue()[key, value, time] = TimeUnit.SECONDS
            } else {
                set(key, value)
            }
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long
     */
    fun incr(key: String, delta: Long): Long {
        if (delta < 0) {
            throw RedisCacheException(RedisCacheErrorEnum.REDIS_DECR_FAIL)
        }
        val increment = redisTemplate.opsForValue().increment(key, delta)
        return increment ?: 0L
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long
     */
    fun decr(key: String, delta: Long): Long {
        if (delta < 0) {
            throw RedisCacheException(RedisCacheErrorEnum.REDIS_DECR_FAIL)
        }
        val increment = redisTemplate.opsForValue().increment(key, -delta)
        return increment ?: 0L
    }

    /**
     * 批量添加
     *
     * @param map 双列集合数据
     */
    fun batchSet(map: Map<String?, String?>) {
        redisTemplate.opsForValue().multiSet(map)
    }


    /**
     * 批量添加 并且设置失效时间
     *
     * @param map     双列集合数据
     * @param seconds 秒
     */
    fun batchSetOrExpire(map: Map<String?, String?>, seconds: Long) {
        val serializer = redisTemplate.stringSerializer
        redisTemplate.executePipelined(RedisCallback<String?> { connection: RedisConnection? ->
            map.forEach { (key: String?, value: String?) ->
                val keyBytes = Objects.requireNonNull(serializer.serialize(key))
                val valueBytes = Objects.requireNonNull(serializer.serialize(value))

                // 使用opsForValue().set替代set方法
                redisTemplate.opsForValue()[keyBytes.contentToString(), valueBytes] = Duration.ofSeconds(seconds)
            }
            null
        }, serializer)
    }


    /**
     * 批量获取
     *
     * @param list 列表
     * @return [List]<[Object]>
     */
    fun batchGet(list: List<String?>): List<Any>? {
        return redisTemplate.opsForValue().multiGet(list)
    }


    /**
     * Redis批量Delete
     *
     * @param list 列表
     */
    fun batchDelete(list: List<String?>) {
        redisTemplate.delete(list)
    }

    /**
     * **************** String end ****************
     * /
     *
     *
     *
     *
     * / **
     * **************** Map start ****************
     * /
     *
     *
     * / **
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    fun hGet(key: String, item: String): Any? {
        return redisTemplate.opsForHash<Any, Any>()[key, item]
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    fun hmGet(key: String): Map<Any, Any> {
        return redisTemplate.opsForHash<Any, Any>().entries(key)
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    fun hmSet(key: String, map: Map<String, Any>): Boolean {
        try {
            redisTemplate.opsForHash<Any, Any>().putAll(key, map)
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    fun hmSet(key: String, map: Map<String, Any>, time: Long): Boolean {
        try {
            redisTemplate.opsForHash<Any, Any>().putAll(key, map)
            if (time > 0) {
                expire(key, time)
            }
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    fun hSet(key: String, item: String, value: Any): Boolean {
        try {
            redisTemplate.opsForHash<Any, Any>().put(key, item, value)
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    fun hSet(key: String, item: String, value: Any, time: Long): Boolean {
        try {
            redisTemplate.opsForHash<Any, Any>().put(key, item, value)
            if (time > 0) {
                expire(key, time)
            }
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    fun hDel(key: String, vararg item: Any?) {
        redisTemplate.opsForHash<Any, Any>().delete(key, *item)
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    fun hHasKey(key: String, item: String): Boolean {
        return redisTemplate.opsForHash<Any, Any>().hasKey(key, item)
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return double
     */
    fun hIncr(key: String, item: String, by: Long): Double {
        return redisTemplate.opsForHash<Any, Any>().increment(key, item, by).toDouble()
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return double
     */
    fun hDecr(key: String, item: String, by: Long): Double {
        return redisTemplate.opsForHash<Any, Any>().increment(key, item, -by).toDouble()
    }


    /**
     * **************** Map end ***************
     * /
     *
     *
     * / ** **************** Set start ****************
     * /
     *
     *
     * / **
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return [Set]<[Object]>
     */
    fun sGet(key: String): Set<Any>? {
        try {
            return redisTemplate.opsForSet().members(key)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return emptySet()
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    fun sHasKey(key: String, value: Any): Boolean {
        try {
            val aBoolean = redisTemplate.opsForSet().isMember(key, value)
            return aBoolean != null && aBoolean
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }


    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    fun sSet(key: String, vararg values: Any?): Long {
        try {
            val add = redisTemplate.opsForSet().add(key, *values)
            return add ?: 0L
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return 0
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    fun sSetAndTime(key: String, time: Long, vararg values: Any?): Long {
        try {
            val count = redisTemplate.opsForSet().add(key, *values)
            if (time > 0) expire(key, time)
            return count ?: 0L
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return 0
        }
    }


    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return long
     */
    fun sGetSetSize(key: String): Long {
        try {
            val size = redisTemplate.opsForSet().size(key)
            return Optional.ofNullable(size).orElse(0L)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return 0
        }
    }


    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    fun setRemove(key: String, vararg values: Any?): Long {
        try {
            val count = redisTemplate.opsForSet().remove(key, *values)
            return Optional.ofNullable(count).orElse(0L)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return 0
        }
    }


    /**
     * **************** Set end ****************
     * /
     *
     *
     * / **
     * **************** List start ***************
     * /
     *
     *
     * / **
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return [List]<[Object]>
     */
    fun lGet(key: String, start: Long, end: Long): List<Any>? {
        try {
            return redisTemplate.opsForList().range(key, start, end)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return emptyList()
        }
    }


    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return long
     */
    fun lGetListSize(key: String): Long {
        try {
            val size = redisTemplate.opsForList().size(key)
            return Optional.ofNullable(size).orElse(0L)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return 0
        }
    }


    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return [Object]
     */
    fun lGetIndex(key: String, index: Long): Any? {
        try {
            return redisTemplate.opsForList().index(key, index)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return null
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    fun lSet(key: String, value: Any): Boolean {
        try {
            redisTemplate.opsForList().rightPush(key, value)
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return boolean
     */
    fun lSet(key: String, value: Any, time: Long): Boolean {
        try {
            redisTemplate.opsForList().rightPush(key, value)
            if (time > 0) expire(key, time)
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    fun lSet(key: String, value: List<Any?>): Boolean {
        try {
            redisTemplate.opsForList().rightPushAll(key, value)
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return boolean
     */
    fun lSet(key: String, value: List<Any?>, time: Long): Boolean {
        try {
            redisTemplate.opsForList().rightPushAll(key, value)
            if (time > 0) expire(key, time)
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return boolean
     */
    fun lUpdateIndex(key: String, index: Long, value: Any): Boolean {
        try {
            redisTemplate.opsForList()[key, index] = value
            return true
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return false
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    fun lRemove(key: String, count: Long, value: Any): Long {
        try {
            val remove = redisTemplate.opsForList().remove(key, count, value)
            return Optional.ofNullable(remove).orElse(0L)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return 0
        }
    }

}