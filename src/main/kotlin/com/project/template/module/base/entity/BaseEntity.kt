package com.project.template.module.base.entity

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

open class BaseEntity(

    @TableId
    val id: Long? = null,

    @Schema(description = "删除标志 0未删除 1删除")
    @TableField("is_deleted")
    val isDeleted: Int? = null,

    @Schema(description = "创建者")
    @TableField(fill = FieldFill.INSERT)
    val createBy: String? = null,


    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    val createOn: LocalDateTime? = null,

    @Schema(description = "更新者")
    @TableField(fill = FieldFill.INSERT)
    val updateBy: String? = null,

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    val updateOn: LocalDateTime? = null,

    )