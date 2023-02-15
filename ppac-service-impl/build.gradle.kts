dependencies {
    implementation(project(":ppac-service-common"))
    implementation(project(":ppac-service-integration:v1"))
    implementation(project(":ppac-service-gateway:countries"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}