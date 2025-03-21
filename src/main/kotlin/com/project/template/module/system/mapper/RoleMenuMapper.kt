package com.project.template.module.system.mapper;

import com.project.template.module.system.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import org.springframework.stereotype.Repository;

/**
 * <p>
 * 角色-菜单关系表 Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Repository
interface RoleMenuMapper : BaseMapper<RoleMenu> {

}
