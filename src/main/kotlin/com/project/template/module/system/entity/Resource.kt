package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 自定义资源表,丰富权限控制体系,提供更加灵活的权限控制
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_resource")
class Resource : BaseEntity() {

    /**
     * 资源编号
     */
    @TableField("resource_code")
    var resourceCode: String? = null

    /**
     * 资源名称
     */
    @TableField("resource_name")
    var resourceName: String? = null

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
        return "Resource{" +
        "resourceCode=" + resourceCode +
        ", resourceName=" + resourceName +
        ", remark=" + remark +
        ", status=" + status +
        "}"
    }
}
