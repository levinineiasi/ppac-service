allprojects{
    dependencies {
        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-noarg")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("io.konform:konform")
        implementation(project(":ppac-service-common"))
        implementation(project(":ppac-service-integration:mvp-api"))
        implementation("com.windpanda.jmapper-framework:jmapper-core")
        implementation("org.junit.jupiter:junit-jupiter")
        implementation("org.springdoc:springdoc-openapi-data-rest")
        implementation("org.springdoc:springdoc-openapi-ui")
        implementation("org.springdoc:springdoc-openapi-kotlin")

        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        testImplementation(kotlin("test"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.mockk:mockk:1.13.4")
        testImplementation("com.ninja-squad:springmockk:3.0.1")
        tasks.test { useJUnitPlatform() }
    }
}

subprojects {
    dependencies {
        implementation(project(":ppac-service-controller"))
    }
}