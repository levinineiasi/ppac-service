import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("kapt")
}

apply(plugin = "org.springframework.boot")
apply(plugin = "kotlin-noarg")

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.codehaus.janino:janino:3.1.9")
    implementation("org.jetbrains.kotlin:kotlin-noarg")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.postgresql:postgresql")
    implementation("com.google.cloud.sql:postgres-socket-factory")
    implementation("com.oracle.database.jdbc:ojdbc8")
    implementation("io.konform:konform")
    implementation("org.hibernate:hibernate-validator")
    implementation("com.h2database:h2")
    implementation(project(":ppac-service-common"))
    implementation(project(":ppac-service-controller:mvp"))
    implementation(project(":ppac-service-impl"))
    testImplementation("org.awaitility:awaitility-kotlin")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.7")
}

tasks.named<BootJar>("bootJar") {
    archiveVersion.set("latest")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

