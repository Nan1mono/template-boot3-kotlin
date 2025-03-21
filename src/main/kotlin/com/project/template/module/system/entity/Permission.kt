package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 资源权限表,包含所有资源和对应的权限
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_permission")
class Permission : BaseEntity() {

    /**
     * 对应的资源编号
     */
    @TableField("code")
    var code: String? = null

    /**
     * 资源类型:menu-菜单资源,button-按钮资源,resource-自定义资源
     */
    @TableField("type")
    var type: String? = null

    /**
     * 权限编号
     */
    @TableField("permission_code")
    var permissionCode: String? = null

    /**
     * 状态 1启用 0停用
     */
    @TableField("status")
    var status: Int? = null

    override fun toString(): String {
        return "Permission{" +
        "code=" + code +
        ", type=" + type +
        ", permissionCode=" + permissionCode +
        ", status=" + status +
        "}"
    }
}
