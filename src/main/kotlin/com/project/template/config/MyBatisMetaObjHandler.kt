package com.project.template.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.project.template.security.utils.SecurityUtils
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MyBatisMetaObjHandler : MetaObjectHandler {
    override fun insertFill(metaObject: MetaObject?) {
        this.strictInsertFill(metaObject, "createBy", String::class.java, SecurityUtils.getUserId().toString())
        this.strictInsertFill(metaObject, "updateBy", String::class.java, SecurityUtils.getUserId().toString())
        this.strictInsertFill(metaObject, "createOn", LocalDateTime::class.java, LocalDateTime.now())
        this.strictInsertFill(metaObject, "updateOn", LocalDateTime::class.java, LocalDateTime.now())
    }


    override fun updateFill(metaObject: MetaObject?) {
        this.strictUpdateFill(metaObject, "updateOn", LocalDateTime::class.java, LocalDateTime.now())
        this.strictInsertFill(metaObject, "updateBy", String::class.java, SecurityUtils.getUserId().toString())
    }
}