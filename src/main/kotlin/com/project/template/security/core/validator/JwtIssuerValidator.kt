package com.project.template.security.core.validator

import com.project.template.common.cache.RedisUtils
import com.project.template.security.exception.enum.AuthFailEnum
import com.project.template.security.utils.SecurityUtils
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

class JwtIssuerValidator(private val redisUtils: RedisUtils) : OAuth2TokenValidator<Jwt> {

    companion object {
        private const val ISSUER = "template-trust-user"
    }

    override fun validate(token: Jwt): OAuth2TokenValidatorResult {
        if (ISSUER != token.issuer.toString()) {
            return OAuth2TokenValidatorResult.failure(OAuth2Error(AuthFailEnum.TOKEN_VERIFY_FAIL.buildMessage()))
        }
        // 判断缓存中是否存在，如果不存在，直接失效
        val userId = token.claims["userId"].toString()
        if (!redisUtils.hasKey(SecurityUtils.buildUserCacheKey(userId.toLong()))) {
            return OAuth2TokenValidatorResult.failure(OAuth2Error(AuthFailEnum.TOKEN_EXPIRED.buildMessage()))
        }
        return OAuth2TokenValidatorResult.success()
    }
}