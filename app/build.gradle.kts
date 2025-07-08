import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.internal.config.JVMConfigurationKeys.JVM_TARGET
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.spring").version(deps.kotlin_plugin)
}

group = "com.azul.avd.kbs"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.processResources {
    filesMatching(listOf("application.properties")) {
        expand(project.properties)
    }
}

tasks.getByName<Jar>("jar") {
    archiveClassifier = ""
}