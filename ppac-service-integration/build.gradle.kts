subprojects{
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springdoc:springdoc-openapi-ui")
        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation(project(":ppac-service-common"))
        implementation("io.konform:konform")
        implementation("com.windpanda.jmapper-framework:jmapper-core")
    }
}
