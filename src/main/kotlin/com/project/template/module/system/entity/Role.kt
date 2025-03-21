package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_role")
class Role : BaseEntity() {

    /**
     * 角色编号
     */
    @TableField("role_code")
    var roleCode: String? = null

    /**
     * 角色名称
     */
    @TableField("role_name")
    var roleName: String? = null

    /**
     * 描述
     */
    @TableField("remark")
    var remark: String? = null

    /**
     * 状态 1启用 0停用
     */
    @TableField("status")
    var status: Int? = null

    override fun toString(): String {
        return "Role{" +
        "roleCode=" + roleCode +
        ", roleName=" + roleName +
        ", remark=" + remark +
        ", status=" + status +
        "}"
    }
}
