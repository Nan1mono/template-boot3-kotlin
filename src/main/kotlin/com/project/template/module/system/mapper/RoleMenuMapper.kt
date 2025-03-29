package com.project.template.module.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.project.template.module.system.entity.RoleMenu
import com.project.template.module.system.pojo.bo.MenuTreeBO
import com.project.template.security.core.entity.SecurityRoleMenu
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

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

    /**
     * 根据菜单集合查询相应的菜单集合和其子选项
     *
     * @param roleIdList 角色id集合
     * @return [List]<[SecurityRoleMenu]>
     */
    fun listMenuTreeByRoleIdList(@Param("roleIdList") roleIdList: List<Long?>): List<MenuTreeBO>

    /**
     * 根据父级id查询其下的子菜单
     *
     * @param parentId 父级id
     * @return [List]<[SecurityRoleMenu]>
     */
    fun listMenuTreeByParentId(@Param("parentId") parentId: Long): List<MenuTreeBO>

}
