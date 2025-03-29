package com.project.template.module.system.service;

import com.project.template.module.system.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.template.module.system.pojo.bo.MenuTreeBO

/**
 * <p>
 * 角色-菜单关系表 服务类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
interface RoleMenuService : IService<RoleMenu>{
    fun listMenuTreeByRoleIdList(roleIdList: List<Long?>?): List<MenuTreeBO>
}
