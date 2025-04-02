package com.project.template.security.utils

import com.alibaba.fastjson2.JSON
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.google.common.collect.Maps
import com.project.template.com.project.template.common.log.annotation.Slf4j2
import com.project.template.com.project.template.common.log.annotation.Slf4j2.Companion.log
import com.project.template.security.core.entity.SecurityUserDetail
import com.project.template.security.exception.AuthException
import com.project.template.security.exception.enum.AuthFailEnum
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.util.*

@Component
@Slf4j2
class JwtHelper {
    companion object {
        const val USER_ID = "userId"
        const val NICKNAME = "nickname"
        const val USERNAME = "username"
        const val REAL_NAME = "realName"
        const val ROLE_LIST = "roles"
        const val AUTHORITIES = "authorities"
        const val MENUS = "menus"
        const val IS_NON_LOCKED = "isNonLocked"
        const val IS_ENABLED = "isEnable"
        const val REMOTE_IP = "remoteIp"

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
            securityUserDetail: SecurityUserDetail,
            expired: Long,
            sign: String
        ): String {
            securityUserDetail.user.password = null
            return JWT.create().withSubject(securityUserDetail.user.id.toString())
                .withIssuer(ISSUER)
                .withExpiresAt(Date(System.currentTimeMillis() + expired * 1000))
                .withClaim(USER_ID, securityUserDetail.user.id)
                .withClaim(USERNAME, securityUserDetail.user.username)
                .withClaim(NICKNAME, securityUserDetail.user.nickname ?: "")
                .withClaim(REAL_NAME, securityUserDetail.user.realName ?: "")
                .withClaim(ROLE_LIST, JSON.toJSONString(securityUserDetail.roleList))
                .withClaim(AUTHORITIES, JSON.toJSONString(securityUserDetail.authorities))
                .withClaim(MENUS, JSON.toJSONString(securityUserDetail.menuList))
                .withClaim(IS_NON_LOCKED, securityUserDetail.isAccountNonLocked)
                .withClaim(IS_ENABLED, securityUserDetail.isEnabled)
                .withClaim(REMOTE_IP, securityUserDetail.ip)
                .sign(Algorithm.HMAC256(sign))
        }

        /**
         * 解析token
         */
        fun decrypt(token: String): MutableMap<String, Claim> {
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
                    if (expiresAt != null && expiresAt.after(Date())) {
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
         * 获取账户
         */
        fun getUsername(token: String): String {
            return (JWT.decode(token).claims[USERNAME]
                ?: throw AuthException(AuthFailEnum.USER_USERNAME_EMPTY)).asString()
        }

        /**
         * 获取
         */
        fun getClaim(token: String, key: String): Claim? {
            return JWT.decode(token).claims[key]
        }

        /**
         * 获取
         */
        fun <T> getClaim(token: String, key: String, clazz: Class<T>): T? {
            return getClaim(token, key)?.let { JSON.parseObject(it.asString(), clazz) }
        }

    }
}