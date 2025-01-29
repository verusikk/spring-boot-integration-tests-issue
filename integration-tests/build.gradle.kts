group = "com.azul.avd.kbs"
version = "1.0-SNAPSHOT"
kotlin {
    jvmToolchain(deps.javaMajorVersion)
}

sourceSets {
    test {
        resources {
            srcDirs(project(":app").sourceSets["main"].resources.srcDirs)
        }
    }
}

tasks.processTestResources {
    filesMatching(listOf("application.properties")) {
        expand(project.properties)
    }
}

dependencies {
    testImplementation("org.apache.maven.resolver:maven-resolver-api")
    testImplementation(project(":app"))
    testImplementation(platform("io.qameta.allure:allure-bom:${deps.allure}"))
    testImplementation("io.qameta.allure:allure-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.assertj:assertj-core:${deps.assertj}")
    testImplementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("ch.qos.logback:logback-core:1.5.16")

}

