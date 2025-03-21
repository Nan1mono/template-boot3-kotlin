package com.project.template

import com.baomidou.mybatisplus.generator.FastAutoGenerator
import com.baomidou.mybatisplus.generator.config.DataSourceConfig
import com.baomidou.mybatisplus.generator.config.OutputFile
import com.project.template.Generator.Companion.DATA_SOURCE_CONFIG
import com.project.template.Generator.Companion.getTable
import com.project.template.module.base.entity.BaseEntity
import org.springframework.stereotype.Repository
import java.util.*

class Generator {
    companion object {
        val DATA_SOURCE_CONFIG = DataSourceConfig.Builder(
            "jdbc:mysql://192.168.66.130:3306/template_mysql8?useSSL=false&serverTimezone=GMT%2B8",
            "root",
            "123456"
        )

        fun getTable(tableName:String): List<String> {
            return if ("all" == tableName ) Collections.emptyList() else tableName.split(",")
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val projectPath = System.getProperty("user.dir")
            val enumMap = EnumMap<OutputFile, String>(OutputFile::class.java)
            enumMap[OutputFile.xml] = "$projectPath/src/main/resources/mapper/"
            FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig { builder ->
                    builder.enableKotlin().author("lee").outputDir("$projectPath/src/main/kotlin")
                }
                .packageConfig { scanner, builder ->
                    val moduleName = scanner.apply("请输入模块名：")
                    builder.parent("com.project.template.module.$moduleName")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .controller("controller")
                        .entity("entity")
                        .pathInfo(enumMap)
                }
                .strategyConfig { scanner, builder ->
                    val tableName = scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")
                    builder.addTablePrefix("sys_", "t_")
                        .addInclude(getTable(tableName))
                        .serviceBuilder().formatServiceFileName("%sService")
                        .controllerBuilder().enableRestStyle().entityBuilder()
                        .disableSerialVersionUID()
                        .superClass(BaseEntity::class.java)
                        .addSuperEntityColumns(
                            "id",
                            "create_by",
                            "create_on",
                            "update_by",
                            "update_on",
                            "is_deleted"
                        )
                        .enableTableFieldAnnotation().mapperBuilder().mapperAnnotation(Repository::class.java)
                        .build()
                }
                .execute()
        }
    }
}