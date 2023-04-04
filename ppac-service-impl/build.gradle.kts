dependencies {
    implementation(project(":ppac-service-common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate:hibernate-envers")
    implementation("com.h2database:h2")
    implementation("com.oracle.database.jdbc:ojdbc8")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.hibernate:hibernate-validator")
    implementation("io.konform:konform")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springdoc:springdoc-openapi-data-rest")
    implementation("org.springdoc:springdoc-openapi-ui")
    implementation("org.springdoc:springdoc-openapi-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    implementation("com.windpanda.jmapper-framework:jmapper-core")
}
