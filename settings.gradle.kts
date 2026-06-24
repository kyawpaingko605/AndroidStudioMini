pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.android") version "2.0.0"  // ← ဒီမှာ version ထည့်ပါ
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidStudioMini"
include(":app")
