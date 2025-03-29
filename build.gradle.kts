plugins {
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.1.10"
}

group = "com.project.template"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    // MyBatis-Plus
    implementation("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.10.1")
    implementation("com.baomidou:mybatis-plus-jsqlparser-4.9:3.5.10.1")
    implementation("com.baomidou:mybatis-plus-generator:3.5.10.1")
    // Database
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.alibaba:druid-spring-boot-starter:1.1.22")
    // JSON
    implementation("com.alibaba.fastjson2:fastjson2:2.0.40")
    // Utilities
    implementation("org.apache.velocity:velocity-engine-core:2.3")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("org.codehaus.janino:janino:3.1.11")
    // JWT
    implementation("com.auth0:java-jwt:4.4.0")
    // OpenAPI/Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    // Configuration Processor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}