package com.project.template.common.cache.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
@EnableCaching
open class RedisConfig {

    /**
     * 设置RedisTemplate规则
     *
     * @param redisConnectionFactory redis连接工厂
     * @return [RedisTemplate]<[Object], [Object]>
     */
    @Bean
    open fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<Any, Any> {
        val redisTemplate = RedisTemplate<Any, Any>()
        redisTemplate.connectionFactory = redisConnectionFactory

        //解决查询缓存转换异常的问题
        val om = ObjectMapper()

        // 解决LocalDateTime序列化问题
        //LocalDatetime序列化
        val timeModule = JavaTimeModule()
        timeModule.addDeserializer(
            LocalDate::class.java,
            LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        timeModule.addDeserializer(
            LocalDateTime::class.java,
            LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        timeModule.addSerializer(
            LocalDate::class.java,
            LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        timeModule.addSerializer(
            LocalDateTime::class.java,
            LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )

        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        om.registerModule(timeModule)

        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )
        val jackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer(om)
        //序列号key value
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = jackson2JsonRedisSerializer
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = jackson2JsonRedisSerializer

        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    /**
     * 设置CacheManager缓存规则
     *
     * @param factory
     * @return
     */
    @Bean
    open fun cacheManager(factory: RedisConnectionFactory): CacheManager {
        val redisSerializer: RedisSerializer<String> = StringRedisSerializer()
        //解决查询缓存转换异常的问题
        val om = ObjectMapper()
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        om.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )
        val jackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer(om)
        // 配置序列化（解决乱码的问题）,过期时间15天
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofDays(15))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
            .disableCachingNullValues()
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build()
    }

}