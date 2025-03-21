package com.project.template.module.system.mapper;

import com.project.template.module.system.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import org.springframework.stereotype.Repository;

/**
 * <p>
 * 资源权限表,包含所有资源和对应的权限 Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Repository
interface PermissionMapper : BaseMapper<Permission> {

}
