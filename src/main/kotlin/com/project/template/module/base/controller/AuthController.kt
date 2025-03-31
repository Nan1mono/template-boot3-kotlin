package com.project.template.module.base.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "登录与验证模块")
class AuthController(
    private val authenticationConfiguration: AuthenticationConfiguration
) {

//    @PostMapping("/login")
//    @Operation(summary = "登录")
//    fun login(@RequestBody authLoginDTO: AuthLoginDTO): Result<Authentication> {
//        val authenticate = authenticationConfiguration.authenticationManager.authenticate(
//            UsernamePasswordAuthenticationToken(
//                authLoginDTO.username,
//                authLoginDTO.password
//            )
//        )
//        return Result.success(authenticate)
//    }

}