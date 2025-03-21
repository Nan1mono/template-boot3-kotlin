package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 基础权限表
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_operation")
class Operation : BaseEntity() {

    /**
     * 操作/选项编号
     */
    @TableField("operation_code")
    var operationCode: String? = null

    /**
     * 操作/选项名称
     */
    @TableField("operation_name")
    var operationName: String? = null

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
        return "Operation{" +
        "operationCode=" + operationCode +
        ", operationName=" + operationName +
        ", remark=" + remark +
        ", status=" + status +
        "}"
    }
}
