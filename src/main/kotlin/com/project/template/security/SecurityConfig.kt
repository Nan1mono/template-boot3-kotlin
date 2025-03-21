package com.project.template.security

import com.project.template.module.system.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Configuration
@EnableWebSecurity
open class SecurityConfig(userService: UserService) {
}