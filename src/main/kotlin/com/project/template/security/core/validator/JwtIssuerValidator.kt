package com.project.template.security.core.validator

import com.project.template.security.exception.enum.AuthFailEnum
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

class JwtIssuerValidator:OAuth2TokenValidator<Jwt> {

    companion object {
        private const val ISSUER = "template-trust-user"
    }

    override fun validate(token: Jwt): OAuth2TokenValidatorResult {
        if (ISSUER != token.issuer.toString()) {
            return OAuth2TokenValidatorResult.failure(OAuth2Error(AuthFailEnum.TOKEN_VERIFY_FAIL.buildMessage()))
        }
        return OAuth2TokenValidatorResult.success()
    }
}