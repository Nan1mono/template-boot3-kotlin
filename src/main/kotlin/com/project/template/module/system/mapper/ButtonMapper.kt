package com.project.template.module.system.mapper;

import com.project.template.module.system.entity.Button;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import org.springframework.stereotype.Repository;

/**
 * <p>
 * 按钮资源表,控制被限制的按钮 Mapper 接口
 * </p>
 *
 * @author lee
 * @since 2025-03-21
 */
@Repository
interface ButtonMapper : BaseMapper<Button> {

}
