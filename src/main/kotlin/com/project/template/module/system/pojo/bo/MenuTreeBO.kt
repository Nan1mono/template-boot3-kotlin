package com.project.template.module.system.pojo.bo

import com.project.template.security.core.entity.SecurityRoleMenu
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 菜单树业务实体
 */
class MenuTreeBO(
    @Schema(description = "菜单id")
    var menuId: Long? = null,

    @Schema(description = "菜单编号")
    var menuCode: String? = null,

    @Schema(description = "菜单名称")
    var menuName: String? = null,

    @Schema(description = "菜单层级")
    var level: Int? = null,

    @Schema(description = "父级菜单id")
    var parentId: Long? = null,

    @Schema(description = "主要路径")
    var path: String? = null,

    @Schema(description = "路由路径")
    var uri: String? = null,

    @Schema(description = "顺序")
    var sorted: Int? = null,

    @Schema(description = "子菜单")
    var roleMenuList: List<MenuTreeBO>? = null
)