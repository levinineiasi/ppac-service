import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.gitlab.arturbosch.detekt") apply false
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    kotlin("plugin.noarg") apply false
    idea
    java
}

allprojects {

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    group = "com.levi9.ppac.service"

    tasks.withType<Detekt> {
        parallel = true
        jvmTarget = "17"
        config.setFrom(rootProject.file("detekt.yml"))
        dependsOn(tasks.withType<KotlinCompile>())
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    repositories {
        mavenCentral()
        maven { setUrl("https://repo1.maven.org/maven2") }
        google()
        maven { setUrl("https://mvnrepository.com/artifact") }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlinx.html") }
    }

    dependencyManagement {
        imports {
            mavenBom("io.projectreactor:reactor-bom:${CoreVersion.REACTOR_BOM}")
            mavenBom("org.springframework.boot:spring-boot-starter-parent:${CoreVersion.SPRING_BOOT}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${CoreVersion.SPRING_CLOUD}")
        }
        dependencies {
            dependency("ch.qos.logback.contrib:logback-jackson:${CoreVersion.LOGBACK_CONTRIB}")
            dependency("ch.qos.logback.contrib:logback-json-classic:${CoreVersion.LOGBACK_CONTRIB}")
            dependency("com.fasterxml.jackson.core:jackson-annotations:${CoreVersion.JACKSON}")
            dependency("com.fasterxml.jackson.core:jackson-core:${CoreVersion.JACKSON}")
            dependency("com.fasterxml.jackson.core:jackson-databind:${CoreVersion.JACKSON}")
            dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${CoreVersion.JACKSON}")
            dependency("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${CoreVersion.JACKSON}")
            dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${CoreVersion.JACKSON}")
            dependency("com.fasterxml.jackson.module:jackson-module-kotlin:${CoreVersion.JACKSON}")
            dependency("com.fasterxml.jackson.module:jackson-module-parameter-names:${CoreVersion.JACKSON}")
            dependency("com.github.ben-manes.caffeine:caffeine:${CoreVersion.CAFFEINE}")
            dependency("com.sun.xml.bind:jaxb-core:${CoreVersion.JAXB_IMPL}")
            dependency("com.sun.xml.bind:jaxb-impl:${CoreVersion.JAXB_IMPL}")
            dependency("com.willowtreeapps.assertk:assertk-jvm:${CoreVersion.ASSERTK_JVM}")
            dependency("io.konform:konform:${CoreVersion.KONFORM}")
            dependency("org.hamcrest:java-hamcrest:${CoreVersion.JAVA_HAMCREST}")
            dependency("org.springframework.boot:spring-boot-starter-cache:${CoreVersion.SPRING_BOOT}")
            dependency("org.springframework.boot:spring-boot-starter-validation:${CoreVersion.SPRING_BOOT}")
            dependency("org.awaitility:awaitility-kotlin:${CoreVersion.AWAITILITY}")
            dependency("com.google.cloud.sql:postgres-socket-factory:${CoreVersion.CLOUD_SQL_SOCKET_FACTORY}")
            dependency("com.oracle.database.jdbc:ojdbc8:${CoreVersion.ORACLE}")
            dependency("org.hibernate:hibernate-validator:${CoreVersion.HIBERNATE_VALIDATOR}")
            dependency("org.junit.jupiter:junit-jupiter-engine:${CoreVersion.JUNIT}")
            dependency("org.junit.jupiter:junit-jupiter:${CoreVersion.JUNIT}")
            dependency("org.junit.jupiter:junit-jupiter-api:${CoreVersion.JUNIT}")
            dependency("org.springdoc:springdoc-openapi-data-rest:${CoreVersion.OPENAPI}")
            dependency("org.springdoc:springdoc-openapi-ui:${CoreVersion.OPENAPI}")
            dependency("org.springdoc:springdoc-openapi-kotlin:${CoreVersion.OPENAPI}")
        }
    }

    tasks {
        test {
            useJUnitPlatform {
                includeEngines("junit-jupiter")
                excludeEngines("junit-vintage")
            }
            testLogging.showStandardStreams = false
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
            reports.html.required.set(false)
            reports.junitXml.required.set(false)
            jvmArgs("-Dspring.profiles.active=test")
            systemProperty("junit.jupiter.execution.parallel.enabled", "true")
            // TODO find a way yo run all unit tests in parallel
//            systemProperty("junit.jupiter.execution.parallel.mode.default", "same_thread")
//            systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "concurrent")
        }
    }

    extensions.findByName("buildScan")?.withGroovyBuilder {
        setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
        setProperty("termsOfServiceAgree", "yes")
    }
}

