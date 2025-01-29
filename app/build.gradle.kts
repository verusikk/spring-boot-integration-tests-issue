import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.spring").version(deps.kotlin_plugin)
}

group = "com.azul.avd.kbs"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
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