package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.Role;
import com.project.template.module.system.mapper.RoleMapper;
import com.project.template.module.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class RoleServiceImpl : ServiceImpl<RoleMapper, Role>(), RoleService {

}
