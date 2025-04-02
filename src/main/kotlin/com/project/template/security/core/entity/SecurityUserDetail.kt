package com.project.template.security.core.entity

import com.alibaba.fastjson2.util.DateUtils
import com.google.common.collect.Lists
import com.project.template.module.system.entity.User
import com.project.template.security.constant.UserStatusEnum
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
    private var username: String,
    private var password: String?,
    var token: String = "",
    var roleList: List<SecurityUserRole> = Lists.newArrayList<SecurityUserRole>(),
    var authorities: List<SimpleGrantedAuthority> = Lists.newArrayList(),
    var menuList: List<SecurityRoleMenu> = Lists.newArrayList(),
    var ip: String? = ""
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password ?: ""
    }

    override fun getUsername(): String {
        return username
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
        return user.isLocked == UserStatusEnum.NORMAL.code
    }

    /**
     * 账户锁定时间
     */
    fun getLockedDateTime(): String? {
        return user.lockDatetime?.let { DateUtils.format(it, "yyyy-MM-dd HH:mm:ss") }
    }

    fun cleanPassword() {
        this.user.password = null
        this.password = null
    }


}