package com.project.template

import com.project.template.com.project.template.common.log.annotation.Slf4j2
import com.project.template.com.project.template.common.log.annotation.Slf4j2.Companion.log
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@Slf4j2
@SpringBootApplication
open class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
            log.info("swagger json url:{}", "http://localhost:8250/v3/api-docs")
            log.info("swagger doc.html url:{}", "http://localhost:8250/doc.html")
        }
    }

}