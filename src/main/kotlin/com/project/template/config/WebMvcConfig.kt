package com.project.template.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebMvcConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedMethods("*").allowedMethods("*").allowedHeaders("*")
            .maxAge(3600)
    }

}