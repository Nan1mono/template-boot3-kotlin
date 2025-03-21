package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.User;
import com.project.template.module.system.mapper.UserMapper;
import com.project.template.module.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号/用户/管理员表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class UserServiceImpl : ServiceImpl<UserMapper, User>(), UserService {

}
