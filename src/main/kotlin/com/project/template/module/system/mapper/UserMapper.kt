package com.project.template.module.system.mapper;

import com.project.template.module.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import org.springframework.stereotype.Repository;

/**
 * <p>
 * 账号/用户/管理员表 Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Repository
interface UserMapper : BaseMapper<User> {

}
