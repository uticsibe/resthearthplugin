import com.sun.xml.internal.ws.encoding.policy.FastInfosetFeatureConfigurator.enabled
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.publish.PublishingExtension

group = "com.dxc"
version = "0.0.1-SNAPSHOT"
description = "restheart_api"
java.sourceCompatibility = JavaVersion.VERSION_1_8


plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    `java-library`
    `maven-publish`
    signing
}


repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://repo1.maven.org/maven2") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}


dependencies {
    val kotlinxCoroutinesVersion="1.3.72"
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.zeebe.spring:spring-zeebe-starter:0.22.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.json:json:20190722")
    implementation("org.apache.httpcomponents:httpclient:4.5.6")
    implementation("org.springframework.boot:spring-boot-starter-web-services") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("net.logstash.logback:logstash-logback-encoder:4.11")
    implementation("io.springfox:springfox-core:2.9.2")
    implementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xsanitize-parentheses")
        jvmTarget = "1.8"
    }
}


publishing {
    publications {
        create<MavenPublication>("binary") {
            artifact("build/classes/kotlin/main/com/dxc/omdpskotlin/api/Filters.class")
        }
    }
    repositories {
        // change URLs to point to your repos, e.g. http://my.org/repo
        maven {
            name = "external"
            url = uri("https://repo1.maven.org/maven2")
        }
    }
}