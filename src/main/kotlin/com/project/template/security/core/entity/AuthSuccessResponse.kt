package com.project.template.security.core.entity

import com.project.template.security.utils.JwtHelper

class AuthSuccessResponse(
    var token: String? = null,
    var userId: Long? = null,
    var username: String? = null,
    var nickname: String? = null,
    var realName: String? = null,
    var roleList: ArrayList<*>? = null,
    var authorities: ArrayList<*>? = null,
    var menuList: ArrayList<*>? = null
) {
    companion object {
        fun build(token: String): AuthSuccessResponse {
            return AuthSuccessResponse().apply {
                userId = JwtHelper.getUserId(token)
                username = JwtHelper.getClaim(token, JwtHelper.USERNAME, String::class.java)
                nickname = JwtHelper.getClaim(token, JwtHelper.NICKNAME, String::class.java)
                realName = JwtHelper.getClaim(token, JwtHelper.REAL_NAME, String::class.java)
                roleList = JwtHelper.getClaim(token, JwtHelper.ROLE_LIST, ArrayList::class.java)
                menuList = JwtHelper.getClaim(token, JwtHelper.MENUS, ArrayList::class.java)
                authorities = JwtHelper.getClaim(token, JwtHelper.AUTHORITIES, ArrayList::class.java)
                this.token = token
            }
        }
    }
}