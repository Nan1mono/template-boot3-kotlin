package com.project.template.security.core.entity

import com.project.template.module.system.entity.Role
import io.swagger.v3.oas.annotations.media.Schema

class SecurityUserRole(
    @Schema(description = "用户id")
    var userId: Long? = null,

    @Schema(description = "角色id")
    var roleId: Long? = null,

    @Schema(description = "角色编号")
    var roleCode: String? = null,

    @Schema(description = "角色名称")
    var roleName: String? = null
)