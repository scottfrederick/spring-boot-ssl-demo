import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.buildpack.platform.build.PullPolicy

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

repositories {
	mavenLocal()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.apache.httpcomponents.client5:httpclient5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}


tasks.named<BootBuildImage>("bootBuildImage") {
	imageName.set("localhost:5001/${project.name}:${project.version}")
	pullPolicy.set(PullPolicy.IF_NOT_PRESENT)
	publish.set(true)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
