package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.Resource;
import com.project.template.module.system.mapper.ResourceMapper;
import com.project.template.module.system.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自定义资源表,丰富权限控制体系,提供更加灵活的权限控制 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class ResourceServiceImpl : ServiceImpl<ResourceMapper, Resource>(), ResourceService {

}
