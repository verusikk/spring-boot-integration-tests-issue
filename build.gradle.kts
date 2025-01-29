import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import java.io.ByteArrayOutputStream

plugins {
	kotlin("jvm").version(deps.kotlin_plugin)
	//`maven-publish`
	id("io.spring.dependency-management").version(deps.spring_dep_management_plugin)
	id("org.springframework.boot").version(deps.spring_boot_plugin)
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	//apply(plugin = "maven-publish")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.springframework.boot")

	sourceSets {
		test {
			resources {
				srcDirs(rootProject.sourceSets["test"].resources.srcDirs)
			}
		}
	}

	dependencies {
		implementation(platform("org.apache.maven.resolver:maven-resolver:${deps.maven_resolver}"))

        implementation("org.springframework.boot:spring-boot-starter-batch")
        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")

		implementation("com.h2database:h2")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
	}

	tasks.withType<Copy> {
		// for shared resources
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		//systemProperty("junit.jupiter.execution.parallel.enabled", "false")
		if (project.properties.contains("junit.jupiter.execution.parallel.enabled")) {
			systemProperty("junit.jupiter.execution.parallel.enabled",
				project.properties["junit.jupiter.execution.parallel.enabled"]!!)
		}
		if (systemProperties["junit.jupiter.execution.parallel.enabled"] == null) {
			// Run separate test classes within a module in parallel
			systemProperty("junit.jupiter.execution.parallel.enabled", "true")
			// Multiply CPU cores x2
			systemProperty("junit.jupiter.execution.parallel.config.dynamic.factor", "2")
			// Run separate tests in same classes in parallel
			systemProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")
		}
		doFirst {
			systemProperty("module.name", project.name)
		}
	}

	dependencyManagement {
		// improves build time
		// see https://discuss.gradle.org/t/what-is-detachedconfiguration-i-have-a-lots-of-them-for-each-subproject-and-resolving-them-takes-95-of-build-time/31595/9
		val applyExclusions = System.getProperty("apply.exclusions.enabled", "false")
		applyMavenExclusions(applyExclusions.toBoolean())
	}
}

allprojects {
	repositories {
		mavenCentral()
		maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
	}
}

