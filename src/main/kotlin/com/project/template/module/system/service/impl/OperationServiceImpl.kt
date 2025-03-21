package com.project.template.module.system.service.impl;

import com.project.template.module.system.entity.Operation;
import com.project.template.module.system.mapper.OperationMapper;
import com.project.template.module.system.service.OperationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础权限表 服务实现类
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Service
open class OperationServiceImpl : ServiceImpl<OperationMapper, Operation>(), OperationService {

}
