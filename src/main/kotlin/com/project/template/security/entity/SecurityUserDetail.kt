package com.project.template.security.entity

import com.project.template.module.system.entity.User
import com.project.template.security.constant.UserStatusEnum
import com.project.template.security.exception.enum.AuthExceptionEnum
import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SecurityUserDetail(
    private val user: User,
    var token: String,
    private val authorities: MutableCollection<SimpleGrantedAuthority>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities;
    }

    override fun getPassword(): String {
        if (StringUtils.isBlank(user.password)) {
            throw SecurityException(
                String.format(
                    "[%s]-%s",
                    AuthExceptionEnum.USER_PASSWORD_EMPTY.code,
                    AuthExceptionEnum.USER_PASSWORD_EMPTY.message
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
                    AuthExceptionEnum.USER_USERNAME_EMPTY.code,
                    AuthExceptionEnum.USER_USERNAME_EMPTY.message
                )
            )
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


}