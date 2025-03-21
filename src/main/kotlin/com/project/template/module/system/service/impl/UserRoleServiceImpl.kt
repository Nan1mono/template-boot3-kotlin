package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.UserRole;
import com.project.template.module.system.mapper.UserRoleMapper;
import com.project.template.module.system.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号/用户/管理员-角色关系表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class UserRoleServiceImpl : ServiceImpl<UserRoleMapper, UserRole>(), UserRoleService {

}
