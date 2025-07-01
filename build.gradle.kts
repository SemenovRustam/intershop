plugins {
	id("org.springframework.boot") version "3.5.0" apply false
	id("io.spring.dependency-management") version "1.1.7" apply false
	id("org.openapi.generator") version "7.12.0"
}

allprojects {
	apply(plugin = "java")

	group = "com.praktikum.semenov"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}

	configure<JavaPluginExtension> {
		toolchain {
			languageVersion.set(JavaLanguageVersion.of(21))
		}
	}
}

subprojects {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
}