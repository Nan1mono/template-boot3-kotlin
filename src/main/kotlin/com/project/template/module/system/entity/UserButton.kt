package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 账号/用户/管理员-角按钮关系表
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_user_button")
class UserButton : BaseEntity() {

    /**
     * 用户id
     */
    @TableField("user_id")
    var userId: Long? = null

    /**
     * 按钮id
     */
    @TableField("button_id")
    var buttonId: Long? = null

    override fun toString(): String {
        return "UserButton{" +
        "userId=" + userId +
        ", buttonId=" + buttonId +
        "}"
    }
}
