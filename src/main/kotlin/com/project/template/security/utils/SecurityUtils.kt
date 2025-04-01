package com.project.template.security.utils

import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * security工具类
 * 用户密码的加密，解密，匹配校验等
 */
class SecurityUtils {

    companion object {

        private const val USER_CACHE_KEY = "template:auth:user:"

        // 设置加密器，这个设置决定了密码将以何种方式进行加密
        private val PASSWORD_ENCODER = BCryptPasswordEncoder()

        /**
         * 清楚security上下文用户数据
         */
        fun clearUser() {
            SecurityContextHolder.clearContext()
        }

        /**
         * 从security上下文中获取原生user
         */
        fun getUserId(): Long {
            return JwtHelper.getUserId(SecurityContextHolder.getContext().authentication.principal.toString())
        }

        /**
         * 加密密码
         */
        fun encodePassword(password: String): String {
            return PASSWORD_ENCODER.encode(password)
        }

        /**
         * 校验密码是否匹配
         * @return true-匹配，false-不匹配
         */
        fun matchPassword(rawPassword: String, encodedPassword: String?): Boolean {
            if (StringUtils.isBlank(encodedPassword)) {
                return false
            }
            return PASSWORD_ENCODER.matches(rawPassword, encodedPassword)
        }

        fun buildUserCacheKey(id: Long?): String {
            return USER_CACHE_KEY.plus(id)
        }
    }

}