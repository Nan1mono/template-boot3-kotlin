package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.Permission;
import com.project.template.module.system.mapper.PermissionMapper;
import com.project.template.module.system.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源权限表,包含所有资源和对应的权限 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class PermissionServiceImpl : ServiceImpl<PermissionMapper, Permission>(), PermissionService {

}
