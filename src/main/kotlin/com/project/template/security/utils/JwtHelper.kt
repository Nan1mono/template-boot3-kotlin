package com.project.template.security.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.google.common.collect.Maps
import com.project.template.com.project.template.common.log.annotation.Slf4j2
import com.project.template.com.project.template.common.log.annotation.Slf4j2.Companion.log
import com.project.template.security.exception.enum.AuthFailEnum
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.util.*

@Component
@Slf4j2
class JwtHelper {
    companion object {
        private const val USER_ID = "userId"
        private const val NICKNAME = "nickname"
        private const val USERNAME = "username"
        private const val REAL_NAME = "realName"

        // 设置发行者
        private const val ISSUER = "template-trust-user"

        /**
         * 创建令牌
         * @param [userId] 用户 ID
         * @param [username] 用户名
         * @param [nickName] 昵称
         * @param [realName] 真实姓名
         * @param [expired] 过期
         * @param [sign] 签名（密钥）
         * @return [String]
         */
        fun createToken(
            userId: Long,
            username: String,
            nickName: String?,
            realName: String?,
            expired: Long,
            sign: String
        ): String {
            return JWT.create().withSubject(userId.toString())
                .withIssuer(ISSUER)
                .withExpiresAt(Date(System.currentTimeMillis() + expired * 1000))
                .withClaim(USER_ID, userId)
                .withClaim(USERNAME, username)
                .withClaim(NICKNAME, nickName?:"")
                .withClaim(REAL_NAME, realName?:"")
                .sign(Algorithm.HMAC256(sign))
        }

        /**
         * 解析token
         */
        private fun decrypt(token: String): MutableMap<String, Claim> {
            if (StringUtils.isBlank(token)) {
                return Maps.newHashMap<String, Claim>()
            }
            try {
                return JWT.decode(token).claims
            } catch (e: JWTDecodeException) {
                log.error(AuthFailEnum.TOKEN_DECODE_FAIL.buildMessage())
                return Maps.newHashMap()
            }
        }

        /**
         * token校验，包含
         * 1、发行者是否匹配
         * 2、签名是否匹配
         * 3、token是否过期
         */
        fun verify(token: String, sign: String): Boolean {
            try {
                // 签名算法对象
                val algorithm = Algorithm.HMAC256(sign)
                // 校验器
                JWT.require(algorithm).withIssuer(ISSUER).build().verify(token).run {
                    val expiresAt = this.expiresAt
                    if (expiresAt != null && expiresAt.before(Date())) {
                        return true
                    }
                    log.error(AuthFailEnum.TOKEN_EXPIRED.buildMessage())
                    return false
                }
            } catch (e: JWTVerificationException) {
                log.error(AuthFailEnum.TOKEN_VERIFY_FAIL.buildMessage())
                return false
            } catch (e: JWTDecodeException) {
                log.error(AuthFailEnum.TOKEN_DECODE_FAIL.buildMessage())
                return false
            }
        }

        /**
         * 获取用户ID
         */
        fun getUserId(token: String): Long {
            return JWT.decode(token).subject.toLong()
        }

        /**
         * 获取用户名
         */
        fun getUsername(token: String): Any? {
            return decrypt(token)[USERNAME]
        }

    }
}