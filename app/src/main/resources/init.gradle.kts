initscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.gradle.toolchains.foojay-resolver-convention:org.gradle.toolchains.foojay-resolver-convention.gradle.plugin:0.8.0")
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

settingsEvaluated {
    if (!settings.pluginManager.hasPlugin("org.gradle.toolchains.foojay-resolver")
        && !settings.pluginManager.hasPlugin("org.gradle.toolchains.foojay-resolver-convention")
    ) {
        apply<org.gradle.toolchains.foojay.FoojayToolchainsConventionPlugin>()
    }
}
