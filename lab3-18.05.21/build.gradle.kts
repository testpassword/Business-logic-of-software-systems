import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kver = "1.5.10"
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

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    // http://localhost:17502/swagger-ui.html
    implementation("org.springdoc:springdoc-openapi-ui:1.5.4")
    // log
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    // json
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.json:json:20210307")
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.beust:klaxon:5.5")
    // db
    runtimeOnly("org.postgresql:postgresql")
    // other
    implementation("com.rabbitmq:amqp-client:5.12.0")
    implementation("commons-io:commons-io:2.9.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("khttp:khttp:1.0.0")
    // test
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> { useJUnitPlatform() }
