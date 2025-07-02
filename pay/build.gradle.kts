plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.12.0"
}

group = "com.semenov"
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
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
	archiveFileName.set("pay.jar")
	mainClass.set("com.semenov.pay.PayApplication")
}
springBoot {
	mainClass.set("com.semenov.pay.PayApplication")
}

openApiGenerate {
	generatorName = "spring"
	inputSpec = "${projectDir}/src/main/resources/api/pay-api.yaml"
	outputDir = "${buildDir}/generated"
	apiPackage = "com.semenov.pay.api"
	modelPackage = "com.semenov.pay.model"
	apiNameSuffix = "Controller" // добавит суффикс к имени интерфейса
	configOptions = mapOf(
		"interfaceOnly" to "true", // Генерируем только интерфейс
		"useSpringBoot3" to "true",
		"reactive" to "true",
		"useTags" to "true"        // <--- эта опция
	)
	verbose.set(true)
}



sourceSets {
	named("main") {
		java.srcDir("$buildDir/generated/src/main/java")
	}
}

tasks.register("generateAll") {
	dependsOn(":app:openApiGenerate", ":pay:openApiGenerate")
}
