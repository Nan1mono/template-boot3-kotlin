package com.project.template.security.service

import com.google.common.collect.Lists
import com.project.template.module.system.entity.*
import com.project.template.module.system.service.*
import com.project.template.security.core.entity.SecurityRoleMenu
import com.project.template.security.core.entity.SecurityUserDetail
import com.project.template.security.core.entity.SecurityUserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

/**
 * 用于与service/orm交互，获取数据库的用户数据
 * 包括：角色，权限等
 */
@Service
class SecurityDataService(
    private val roleService: RoleService,
    private val userRoleService: UserRoleService,
    private val rolePermissionService: RolePermissionService,
    private val permissionService: PermissionService,
    private val roleMenuService: RoleMenuService
) {

    /**
     * 构建角色权限，包括以下信息：
     * 角色
     * 权限
     * 菜单
     * @param [securityUserDetail] 登录用户信息
     */
    fun buildPermission(securityUserDetail: SecurityUserDetail) {
        // 构建返回体：角色信息和权限信息
        val securityUserRoleList = ArrayList<SecurityUserRole>()
        val authorities = Lists.newArrayList<SimpleGrantedAuthority>()
        // 查询用户对应的所有角色
        val roleIdList = userRoleService.ktQuery().eq(UserRole::userId, securityUserDetail.user.id).list()
            .map { it.roleId }.distinct()
        // 添加用户角色信息
        roleService.ktQuery().`in`(Role::id, roleIdList).list()
            .forEach { role ->
                securityUserRoleList.add(
                    SecurityUserRole(
                        securityUserDetail.user.id,
                        role.id,
                        role.roleCode,
                        role.roleName
                    )
                )
            }
        // 查询用户权限id，并通过id查询所有权限列表
        val permissionIdList = rolePermissionService.ktQuery().`in`(RolePermission::roleId, roleIdList).list()
            .map { it.permissionId }.distinct()
        permissionService.ktQuery().`in`(Permission::id, permissionIdList).list()
            .forEach { permission -> authorities.add(SimpleGrantedAuthority(permission.permissionCode)) }
        // 查询用户菜单
        val securityRoleMenuList =
            SecurityRoleMenu.buildSecurityRoleMenu(roleMenuService.listMenuTreeByRoleIdList(roleIdList))
        securityUserDetail.apply {
            this.roleList = securityUserRoleList
            this.authorities = authorities
            this.menuList = securityRoleMenuList
        }
    }
}