plugins {
    kotlin("jvm")
}

group = "com.hyosakura"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":LLAnalyzer:backend"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}