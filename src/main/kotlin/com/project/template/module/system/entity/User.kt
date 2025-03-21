package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 账号/用户/管理员表
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_user")
class User : BaseEntity() {

    /**
     * 昵称
     */
    @TableField("nickname")
    var nickname: String? = null

    /**
     * 真实姓名
     */
    @TableField("real_name")
    var realName: String? = null

    /**
     * 出生日期
     */
    @TableField("birthday")
    var birthday: LocalDate? = null

    /**
     * 用户名/账号
     */
    @TableField("username")
    var username: String? = null

    /**
     * 密码
     */
    @TableField("password")
    var password: String? = null

    /**
     * 密码到期时间 空代表永不过期
     */
    @TableField("pwd_expiration_time")
    var pwdExpirationTime: LocalDateTime? = null

    /**
     * 状态 1启用 0停用
     */
    @TableField("status")
    var status: Int? = null

    /**
     * 是否被锁定 0未锁定 1锁定
     */
    @TableField("is_locked")
    var isLocked: Int? = null

    /**
     * 账号锁定到期时间，空代表永久锁定
     */
    @TableField("lock_datetime")
    var lockDatetime: LocalDateTime? = null

    override fun toString(): String {
        return "User{" +
        "nickname=" + nickname +
        ", realName=" + realName +
        ", birthday=" + birthday +
        ", username=" + username +
        ", password=" + password +
        ", pwdExpirationTime=" + pwdExpirationTime +
        ", status=" + status +
        ", isLocked=" + isLocked +
        ", lockDatetime=" + lockDatetime +
        "}"
    }
}
