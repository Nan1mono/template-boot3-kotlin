package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 角色-菜单关系表
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_role_menu")
class RoleMenu : BaseEntity() {

    /**
     * 角色id
     */
    @TableField("role_id")
    var roleId: Long? = null

    /**
     * 菜单id
     */
    @TableField("menu_id")
    var menuId: Long? = null

    override fun toString(): String {
        return "RoleMenu{" +
        "roleId=" + roleId +
        ", menuId=" + menuId +
        "}"
    }
}
