pluginManagement {
    val gradlePluginVersion = object {
        val SPRING_BOOT_GRADLE_VERSION = "2.6.1"
        val SPRING_DEPENDENCY_MANAGEMENT_VERSION = "1.0.10.RELEASE"
        val KOTLIN_GRADLE_VERSION = "1.6.10"
        val DETEKT_VERSION = "1.19.0"
        val GRADLE_ENTERPRISE_VERSION = "3.2"
    }
    plugins {
        id("com.gradle.enterprise") version gradlePluginVersion.GRADLE_ENTERPRISE_VERSION
        id("io.gitlab.arturbosch.detekt") version gradlePluginVersion.DETEKT_VERSION
        id("io.spring.dependency-management") version gradlePluginVersion.SPRING_DEPENDENCY_MANAGEMENT_VERSION
        id("org.springframework.boot") version gradlePluginVersion.SPRING_BOOT_GRADLE_VERSION
        kotlin("jvm") version gradlePluginVersion.KOTLIN_GRADLE_VERSION
        kotlin("kapt") version gradlePluginVersion.KOTLIN_GRADLE_VERSION
        kotlin("plugin.allope") version gradlePluginVersion.KOTLIN_GRADLE_VERSION
        kotlin("plugin.jpa") version gradlePluginVersion.KOTLIN_GRADLE_VERSION
        kotlin("plugin.noarg") version gradlePluginVersion.KOTLIN_GRADLE_VERSION
        kotlin("plugin.spring") version gradlePluginVersion.KOTLIN_GRADLE_VERSION
    }
}

plugins {
    id("com.gradle.enterprise")
}

buildCache {
    local {
        directory = File("${rootDir}/build-cache")
        removeUnusedEntriesAfterDays = 1
    }
}

rootProject.name = "ppac-service"
include("ppac-service-assembly")
include("ppac-service-common")
include("ppac-service-controller")
include("ppac-service-controller:mvp")
include("ppac-service-integration")
include("ppac-service-integration:mvp-api")
include("ppac-service-impl")
