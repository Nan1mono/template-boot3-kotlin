package com.project.template.security.entity

import com.google.common.collect.Lists
import com.project.template.module.system.entity.User
import com.project.template.security.constant.UserStatusEnum
import com.project.template.security.exception.enum.AuthFailEnum
import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 安全用户详细信息
 * @author Lee
 * @date 2025/03/25
 * @constructor 创建[SecurityUserDetail]
 * @param [user] 用户
 * @param [authorities] 当局
 */
class SecurityUserDetail(
    var user: User,
    var token:String = "",
    private val authorities: Collection<SimpleGrantedAuthority> = Lists.newArrayList(),

) : UserDetails {



    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities;
    }

    override fun getPassword(): String {
        if (StringUtils.isBlank(user.password)) {
            throw SecurityException(AuthFailEnum.USER_PASSWORD_EMPTY.buildMessage())
        }
        return user.password!!
    }

    override fun getUsername(): String {
        if (StringUtils.isBlank(user.password)) {
            throw SecurityException(AuthFailEnum.USER_USERNAME_EMPTY.buildMessage())
        }
        return user.password!!
    }

    /**
     * 判单该账号是否已经启用
     * @return [Boolean]
     */
    override fun isEnabled(): Boolean {
        return user.status == UserStatusEnum.ENABLED.code
    }

    /**
     * 帐户是否未锁定，true-未锁定，false-已锁定
     * @return [Boolean]
     */
    override fun isAccountNonLocked(): Boolean {
        return user.status == UserStatusEnum.ENABLED.code
    }


}