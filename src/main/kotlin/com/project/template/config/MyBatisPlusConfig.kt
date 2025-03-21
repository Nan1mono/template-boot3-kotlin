package com.project.template.config

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@MapperScan("com.project.template.**.mapper")
open class MyBatisPlusConfig {

    // 添加mybatis分页插件
    @Bean
    open fun mybatisPlusInterceptor():MybatisPlusInterceptor{
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor())
        return interceptor
    }

}