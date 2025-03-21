package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 按钮资源表,控制被限制的按钮
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_button")
class Button : BaseEntity() {

    /**
     * 菜单id
     */
    @TableField("menu_id")
    var menuId: Long? = null

    /**
     * 按钮编号
     */
    @TableField("button_code")
    var buttonCode: String? = null

    /**
     * 按钮名称
     */
    @TableField("button_name")
    var buttonName: String? = null

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
        return "Button{" +
        "menuId=" + menuId +
        ", buttonCode=" + buttonCode +
        ", buttonName=" + buttonName +
        ", remark=" + remark +
        ", status=" + status +
        "}"
    }
}
