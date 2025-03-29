package com.project.template.security.utils

import com.project.template.module.system.entity.User
import com.project.template.security.core.entity.SecurityUserDetail
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
         * security上下文获取用户登录信息
         */
        fun getUserDetail(): SecurityUserDetail {
            val authentication = SecurityContextHolder.getContext().authentication
            return authentication.principal as SecurityUserDetail
        }

        /**
         * 清楚security上下文用户数据
         */
        fun clearUser() {
            SecurityContextHolder.clearContext()
        }

        /**
         * 从security上下文中获取原生user
         */
        fun getUser(): User {
            return getUserDetail().user
        }

        /**
         * 从security上下文中获取token
         */
        fun getToken(): String {
            return getUserDetail().token
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