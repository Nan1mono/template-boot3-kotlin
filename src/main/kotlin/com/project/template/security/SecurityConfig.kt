package com.project.template.security

import com.project.template.common.cache.RedisUtils
import com.project.template.module.system.entity.User
import com.project.template.module.system.service.UserService
import com.project.template.security.core.entity.SecurityUserDetail
import com.project.template.security.core.handler.UsernameAuthenticationSuccessHandler
import com.project.template.security.core.validator.JwtIssuerValidator
import com.project.template.security.exception.enum.AuthFailEnum
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec


@Configuration
@EnableWebSecurity
open class SecurityConfig(
    @Value("\${template.security.allow.uri}")
    private val uri: ArrayList<String>,
    @Value("\${template.token.sign-key:nan1mono}")
    private val sign: String,
    private val userService: UserService,
    private val redisUtils: RedisUtils
) {

    /**
     * 登录拦截器
     */
    @Bean
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
     * 设置核心过滤器链
     * 所有非白名单请求将经过该过滤器链进行校验
     */
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { it.requestMatchers(*uri.toTypedArray()).permitAll().anyRequest().authenticated() }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt { jwt -> jwt.decoder(jwtDecoder()) } }
        return http.build()
    }

    /**
     * 设置jwt解码器
     * 后续解码工作将交给Spring Security Resource Server进行，以OAuth2标准进行校验
     */
    @Bean
    open fun jwtDecoder(): JwtDecoder {
        val secretKey = SecretKeySpec(sign.toByteArray(), "HmacSHA256")
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build()
            .apply { this.setJwtValidator(JwtIssuerValidator(redisUtils)) }
    }

    /**
     * 配置spring security密码加密器，将用于各类场景的spring security加解密
     */
    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
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