plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.advent"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // https://github.com/spring-projects/spring-boot/issues/43568
    // Force bumping internal Spring Boot dependencies
    implementation("ch.qos.logback:logback-classic:1.5.14+")
    implementation("ch.qos.logback:logback-core:1.5.14+")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
