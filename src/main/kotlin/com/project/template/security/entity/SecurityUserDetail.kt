package com.project.template.security.entity

import com.project.template.module.system.entity.User
import com.project.template.security.exception.enum.SecurityExceptionEnum
import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SecurityUserDetail(
    var user: User,
    var token: String,
    var authorities: MutableCollection<SimpleGrantedAuthority>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities;
    }

    override fun getPassword(): String {
        if (StringUtils.isBlank(user.password)) {
            throw SecurityException(
                String.format(
                    "[%s]-%s",
                    SecurityExceptionEnum.USER_PASSWORD_EMPTY.code,
                    SecurityExceptionEnum.USER_PASSWORD_EMPTY.message
                )
            )
        }
        return user.password!!
    }

    override fun getUsername(): String {
        if (StringUtils.isBlank(user.password)) {
            throw SecurityException(
                String.format(
                    "[%s]-%s",
                    SecurityExceptionEnum.USER_USERNAME_EMPTY.code,
                    SecurityExceptionEnum.USER_USERNAME_EMPTY.message
                )
            )
        }
        return user.password!!
    }
}