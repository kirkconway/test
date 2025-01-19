plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.wiremock:wiremock:3.10.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.4")
    runtimeOnly("com.h2database:h2:2.3.232")

    implementation("org.springframework.boot:spring-boot-starter-web:3.1.4")
    implementation("commons-io:commons-io:2.18.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.4")
//    implementation("org.hibernate.orm:hibernate-core:6.2.5.Final")

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}