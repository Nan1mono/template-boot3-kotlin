package com.project.template.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.template.module.base.entity.BaseEntity;

/**
 * <p>
 * 菜单资源表
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@TableName("sys_menu")
class Menu : BaseEntity() {

    /**
     * 菜单编号
     */
    @TableField("menu_code")
    var menuCode: String? = null

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    var menuName: String? = null

    /**
     * 父级菜单id
     */
    @TableField("parent_id")
    var parentId: Long? = null

    /**
     * 菜单层级
     */
    @TableField("level")
    var level: Int? = null

    /**
     * 主要路径
     */
    @TableField("path")
    var path: String? = null

    /**
     * 路由路径
     */
    @TableField("uri")
    var uri: String? = null

    /**
     * 顺序
     */
    @TableField("sorted")
    var sorted: Int? = null

    /**
     * 状态 1启用 0停用
     */
    @TableField("status")
    var status: Int? = null

    override fun toString(): String {
        return "Menu{" +
        "menuCode=" + menuCode +
        ", menuName=" + menuName +
        ", parentId=" + parentId +
        ", level=" + level +
        ", path=" + path +
        ", uri=" + uri +
        ", sorted=" + sorted +
        ", status=" + status +
        "}"
    }
}
