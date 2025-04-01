package com.project.template.security.core.entity

import com.project.template.security.utils.JwtHelper

class AuthSuccessResponse(
    var token: String,
    var userId: Long,
    var username: String,
    var nickname: String? = null,
    var realName: String? = null,
    var roleList: ArrayList<*>? = null,
    var authorities: ArrayList<*>? = null,
    var menuList: ArrayList<*>? = null
) {
    companion object {
        fun buildWithUsernameToken(token: String): AuthSuccessResponse {
            return AuthSuccessResponse(
                token,
                JwtHelper.getUserId(token),
                JwtHelper.getClaim(token, JwtHelper.USERNAME) ?: ""
            ).apply {
                nickname = JwtHelper.getClaim(token, JwtHelper.NICKNAME)
                realName = JwtHelper.getClaim(token, JwtHelper.REAL_NAME)
                roleList = JwtHelper.getClaim(token, JwtHelper.ROLE_LIST, ArrayList::class.java)
                menuList = JwtHelper.getClaim(token, JwtHelper.MENUS, ArrayList::class.java)
                authorities = JwtHelper.getClaim(token, JwtHelper.AUTHORITIES, ArrayList::class.java)
            }
        }
    }
}