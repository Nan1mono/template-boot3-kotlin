package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.RolePermission;
import com.project.template.module.system.mapper.RolePermissionMapper;
import com.project.template.module.system.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色-权限关系表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class RolePermissionServiceImpl : ServiceImpl<RolePermissionMapper, RolePermission>(), RolePermissionService {

}
