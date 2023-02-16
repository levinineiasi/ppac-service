dependencies {
    implementation(project(":ppac-service-common"))
    implementation(project(":ppac-service-integration:v1"))
    implementation(project(":ppac-service-gateway:countries"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate:hibernate-envers")
    implementation("com.h2database:h2")
    implementation("com.oracle.database.jdbc:ojdbc8")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.hibernate:hibernate-validator")
    implementation("io.konform:konform")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}