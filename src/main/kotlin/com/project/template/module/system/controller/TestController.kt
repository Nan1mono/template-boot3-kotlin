package com.project.template.module.system.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
@Tag(name = "测试接口")
class TestController {

    @Operation(summary = "公开资源")
    @GetMapping("/public/resource")
    fun publicResource(): String {
        return "public resource"
    }

    @Operation(summary = "私有资源")
    @GetMapping("/private/resource")
    fun privateResource(): String {
        return "private resource"
    }

}