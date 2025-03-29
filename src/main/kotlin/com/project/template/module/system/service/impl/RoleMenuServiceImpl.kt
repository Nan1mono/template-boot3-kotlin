package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.RoleMenu;
import com.project.template.module.system.mapper.RoleMenuMapper;
import com.project.template.module.system.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists
import com.project.template.module.system.pojo.bo.MenuTreeBO
import org.apache.commons.collections4.CollectionUtils
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色-菜单关系表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class RoleMenuServiceImpl : ServiceImpl<RoleMenuMapper, RoleMenu>(), RoleMenuService {
    override fun listMenuTreeByRoleIdList(roleIdList: List<Long?>?): List<MenuTreeBO> {
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList()
        }
        return baseMapper.listMenuTreeByRoleIdList(roleIdList!!)
    }

}
