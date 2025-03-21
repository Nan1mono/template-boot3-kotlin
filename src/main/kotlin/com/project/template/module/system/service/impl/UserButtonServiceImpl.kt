package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.UserButton;
import com.project.template.module.system.mapper.UserButtonMapper;
import com.project.template.module.system.service.UserButtonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号/用户/管理员-角按钮关系表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class UserButtonServiceImpl : ServiceImpl<UserButtonMapper, UserButton>(), UserButtonService {

}
