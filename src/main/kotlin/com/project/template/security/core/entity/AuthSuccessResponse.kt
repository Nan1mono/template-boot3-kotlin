package com.project.template.security.core.entity

import com.alibaba.fastjson2.JSON
import com.google.common.collect.Lists
import com.project.template.security.utils.JwtHelper
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AuthSuccessResponse(
    var token: String,
    var userId: Long,
    var username: String,
    var isNonLocked: Boolean,
    var isEnabled: Boolean,
    var remoteIp: String,
    var nickname: String? = null,
    var realName: String? = null,
    var lockDateTime: String? = null,
    var roleList: ArrayList<*>? = null,
    var authorities: ArrayList<*>? = null,
    var menuList: ArrayList<*>? = null
) {
    companion object {
        fun buildWithUsernameToken(token: String): AuthSuccessResponse {
            return AuthSuccessResponse(
                token,
                JwtHelper.getUserId(token),
                JwtHelper.getClaim(token, JwtHelper.USERNAME)?.asString() ?: "",
                JwtHelper.getClaim(token, JwtHelper.IS_NON_LOCKED)?.asBoolean() ?: false,
                JwtHelper.getClaim(token, JwtHelper.IS_ENABLED)?.asBoolean() ?: false,
                JwtHelper.getClaim(token, JwtHelper.REMOTE_IP)?.asString() ?: ""
            ).apply {
                nickname = JwtHelper.getClaim(token, JwtHelper.NICKNAME)?.asString()
                realName = JwtHelper.getClaim(token, JwtHelper.REAL_NAME)?.asString()
                lockDateTime = JwtHelper.getClaim(token, JwtHelper.LOCK_DATE_TIME)?.asString()
                roleList = JwtHelper.getClaim(token, JwtHelper.ROLE_LIST, ArrayList::class.java)
                menuList = JwtHelper.getClaim(token, JwtHelper.MENUS, ArrayList::class.java)
                authorities = JwtHelper.getClaim(token, JwtHelper.AUTHORITIES, ArrayList::class.java)
            }
        }
    }

    fun getSimpleGrantedAuthorities(): ArrayList<SimpleGrantedAuthority> {
        val result = Lists.newArrayList<SimpleGrantedAuthority>()
        authorities?.forEach { e ->
            val authority = JSON.parseObject(JSON.toJSONString(e), SimpleGrantedAuthority::class.java)
            result.add(authority)
        }
        return result
    }
}