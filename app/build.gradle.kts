import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.12.0"
}

group = "com.praktikum.semenov"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework:spring-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
//    implementation("io.lettuce:lettuce-core:6.2.4.RELEASE")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql") // JDBC-драйвер
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    //doc
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootJar> {
    archiveFileName.set("intershop.jar")
    mainClass.set("com.praktikum.semenov.intershop.IntershopApplication")
}
springBoot {
    mainClass.set("com.praktikum.semenov.intershop.IntershopApplication")
}


openApiGenerate {
    generatorName.set("spring")
    inputSpec.set(layout.projectDirectory.file("src/main/resources/api/openapi.yml").asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)
    apiPackage.set("com.praktikum.semenov.generated.api")
    modelPackage.set("com.praktikum.semenov.generated.model")
    configOptions.set(mapOf(
        "interfaceOnly" to "true",
        "useSpringBoot3" to "true",
        "reactive" to "true",
        "java8" to "true",
        "skipValidateSpec" to "true"
    ))
}

sourceSets {
    main {
        java {
            srcDir("${layout.buildDirectory}/generated/src/main/java")
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}

tasks.jar {
    enabled = false
}
