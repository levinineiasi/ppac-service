allprojects {
    dependencies {
        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.apache.httpcomponents:httpclient")
        implementation("org.jetbrains.kotlin:kotlin-noarg")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
        implementation("io.github.openfeign:feign-jackson:11.9.1")
        implementation(project(":ppac-service-common"))
        implementation(project(":ppac-service-integration:v1"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.springfox:springfox-swagger2:3.0.0") // swagger2
    }
}

subprojects {
    dependencies {
        implementation(project(":ppac-service-gateway"))
    }
}