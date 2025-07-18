plugins {
    java
    id("org.springframework.boot") version "3.4.5"
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

val mockitoAgent = configurations.create("mockitoAgent") {}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // https://github.com/spring-projects/spring-boot/issues/43568
    // Force bumping internal Spring Boot dependencies
    implementation("ch.qos.logback:logback-classic:1.5.14+")
    implementation("ch.qos.logback:logback-core:1.5.14+")

    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-collections4:4.5.0-M3")
    implementation("com.google.guava:guava:32.1.3-jre")

    testImplementation("org.mockito:mockito-bom:5.14.0")
    testImplementation("org.mockito:mockito-core")
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}

tasks.withType<Test> {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}
