pluginManagement {
	repositories {
		mavenLocal()
		maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.spring.io/snapshot") }
		gradlePluginPortal()
	}
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.0.20"
    }
}

rootProject.name = "spring-boot-ssl-demo"

include("books-server")
include("books-client")
