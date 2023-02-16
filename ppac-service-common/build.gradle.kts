apply(plugin = "kotlin-jpa")

dependencies {
    implementation("com.h2database:h2")
    implementation("com.oracle.database.jdbc:ojdbc8")
    implementation("io.konform:konform")
    implementation("org.hibernate:hibernate-envers")
    implementation("org.hibernate:hibernate-validator")
    implementation("org.jetbrains.kotlin:kotlin-noarg")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
}