package com.project.template.security

import com.alibaba.fastjson2.JSON
import com.project.template.com.project.template.common.result.Result2
import com.project.template.common.cache.RedisUtils
import com.project.template.module.system.entity.User
import com.project.template.module.system.service.UserService
import com.project.template.security.core.entity.SecurityUserDetail
import com.project.template.security.core.filter.PermissionFilter
import com.project.template.security.core.handler.PermissionAuthenticationFailPoint
import com.project.template.security.core.handler.UsernameAuthenticationSuccessHandler
import com.project.template.security.exception.enum.AuthFailEnum
import com.project.template.security.utils.JwtHelper
import com.project.template.security.utils.SecurityUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
open class SecurityConfig(
    @Value("\${template.security.allow.uri}")
    private val uri: ArrayList<String>,
    @Value("\${template.token.sign-key:nan1mono}")
    private val sign: String,
    private val userService: UserService,
    private val redisUtils: RedisUtils,
    private val permissionFilter: PermissionFilter
) {

    /**
     * 登录拦截器
     */
    @Bean
    @Order(1)
    open fun formLoginFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.securityMatcher("/api/v1/auth/login")
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .formLogin {
                it.loginProcessingUrl("/api/v1/auth/login")
                it.successHandler(UsernameAuthenticationSuccessHandler())
            }
            .csrf { it.disable() }
        return http.build()
    }

    /**
     * 登出拦截器
     */
    @Bean
    @Order(2)
    open fun logoutFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.securityMatcher("/api/v1/auth/logout")
            .logout {
                it.logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler { _, _, authentication ->
                        val userId = JwtHelper.getUserId(authentication.principal.toString())
                        redisUtils.del(SecurityUtils.buildUserCacheKey(userId))
                    }
                    .logoutSuccessHandler { _, response, _ ->
                        val result = JSON.toJSONString(Result2.success("logout success"))
                        response.contentType = "application/json;charset=UTF-8"
                        response.writer.println(result)
                    }
            }
            .csrf { it.disable() }
        return http.build()
    }

    /**
     * 设置核心过滤器链
     * 所有非白名单请求将经过该过滤器链进行校验
     */
    @Bean
    @Order(3)
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { it.requestMatchers(*uri.toTypedArray()).permitAll().anyRequest().authenticated() }
            .exceptionHandling { it.authenticationEntryPoint(PermissionAuthenticationFailPoint()) }
            .addFilterAt(permissionFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    open fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Bean
    open fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
        val user = userService.ktQuery().eq(User::username, username).one()
        user?.let { SecurityUserDetail(it, user.username!!, user.password!!) }
            ?: throw BadCredentialsException(AuthFailEnum.USER_NOT_EXIST.buildMessage())
    }

}