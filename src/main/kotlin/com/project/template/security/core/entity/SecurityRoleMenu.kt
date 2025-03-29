package com.project.template.security.core.entity

import com.google.common.collect.Lists
import com.project.template.module.system.pojo.bo.MenuTreeBO
import io.swagger.v3.oas.annotations.media.Schema
import org.apache.commons.collections4.CollectionUtils

class SecurityRoleMenu(
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
    var roleMenuList: List<SecurityRoleMenu>? = null
) {

    companion object {
        fun buildSecurityRoleMenu(menuTreeBOList: List<MenuTreeBO>): ArrayList<SecurityRoleMenu> {
            val resultList = Lists.newArrayList<SecurityRoleMenu>()
            for (menuTreeBO in menuTreeBOList) {
                val securityRoleMenu = SecurityRoleMenu().apply {
                    menuId = menuTreeBO.menuId
                    menuCode = menuTreeBO.menuCode
                    menuName = menuTreeBO.menuName
                    level = menuTreeBO.level
                    parentId = menuTreeBO.parentId
                    path = menuTreeBO.path
                    uri = menuTreeBO.uri
                    sorted = menuTreeBO.sorted
                }
                if (CollectionUtils.isNotEmpty(menuTreeBO.roleMenuList)) {
                    securityRoleMenu.roleMenuList = buildSecurityRoleMenu(menuTreeBO.roleMenuList!!)
                }
                resultList.add(securityRoleMenu)
            }
            return resultList
        }
    }

}