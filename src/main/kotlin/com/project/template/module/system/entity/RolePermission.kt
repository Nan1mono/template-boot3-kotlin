package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 角色-权限关系表
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_role_permission")
class RolePermission : BaseEntity() {

    /**
     * 角色id
     */
    @TableField("role_id")
    var roleId: Long? = null

    /**
     * 权限id
     */
    @TableField("permission_id")
    var permissionId: Long? = null

    override fun toString(): String {
        return "RolePermission{" +
        "roleId=" + roleId +
        ", permissionId=" + permissionId +
        "}"
    }
}
