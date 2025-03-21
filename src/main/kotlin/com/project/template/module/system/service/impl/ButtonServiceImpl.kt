package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.Button;
import com.project.template.module.system.mapper.ButtonMapper;
import com.project.template.module.system.service.ButtonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 按钮资源表,控制被限制的按钮 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class ButtonServiceImpl : ServiceImpl<ButtonMapper, Button>(), ButtonService {

}
