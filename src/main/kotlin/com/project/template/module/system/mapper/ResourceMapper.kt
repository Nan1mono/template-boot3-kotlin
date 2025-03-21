package com.project.template.module.system.mapper;

import com.project.template.module.system.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import org.springframework.stereotype.Repository;

/**
 * <p>
 * 自定义资源表,丰富权限控制体系,提供更加灵活的权限控制 Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Repository
interface ResourceMapper : BaseMapper<Resource> {

}
