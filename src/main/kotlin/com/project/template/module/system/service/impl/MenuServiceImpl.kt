package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.Menu;
import com.project.template.module.system.mapper.MenuMapper;
import com.project.template.module.system.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单资源表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class MenuServiceImpl : ServiceImpl<MenuMapper, Menu>(), MenuService {

}
