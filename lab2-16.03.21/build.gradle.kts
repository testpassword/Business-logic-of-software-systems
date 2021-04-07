import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kver = "1.4.30"
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version kver
    kotlin("plugin.spring") version kver
    kotlin("plugin.jpa") version kver
    kotlin("plugin.serialization") version kver
}

group = "testpassword"
version = "1.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories { mavenCentral() }

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // log
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    // json
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.json:json:20210307")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // db
    runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> { useJUnitPlatform() }
