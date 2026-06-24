package com.example.androidstudiomini

import java.io.File

/**
 * Dependency Manager - Manages project dependencies
 */
class DependencyManager(private val projectPath: String) {

    /**
     * Get all dependencies
     */
    fun getDependencies(): List<Dependency> {
        val buildGradle = File(projectPath, "app/build.gradle.kts")
        return if (buildGradle.exists()) {
            parseDependencies(buildGradle.readText())
        } else {
            emptyList()
        }
    }

    /**
     * Add dependency
     */
    fun addDependency(dependency: Dependency): Boolean {
        return try {
            val buildGradle = File(projectPath, "app/build.gradle.kts")
            if (!buildGradle.exists()) {
                return false
            }

            val content = buildGradle.readText()
            val dependencyLine = "    implementation(\"${dependency.group}:${dependency.name}:${dependency.version}\")"

            val updatedContent = if (content.contains("dependencies {")) {
                content.replace(
                    "dependencies {",
                    "dependencies {\n$dependencyLine"
                )
            } else {
                content + "\n\ndependencies {\n$dependencyLine\n}"
            }

            buildGradle.writeText(updatedContent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Remove dependency
     */
    fun removeDependency(dependencyId: String): Boolean {
        return try {
            val buildGradle = File(projectPath, "app/build.gradle.kts")
            if (!buildGradle.exists()) {
                return false
            }

            val content = buildGradle.readText()
            val lines = content.split("\n").filter { line ->
                !line.contains(dependencyId)
            }

            buildGradle.writeText(lines.joinToString("\n"))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Update dependency version
     */
    fun updateDependency(dependencyId: String, newVersion: String): Boolean {
        return try {
            val buildGradle = File(projectPath, "app/build.gradle.kts")
            if (!buildGradle.exists()) {
                return false
            }

            val content = buildGradle.readText()
            val regex = Regex("""($dependencyId:[^"]*):[\d.]+""")
            val updatedContent = content.replace(regex, "${'$'}1:$newVersion")

            buildGradle.writeText(updatedContent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get available dependencies
     */
    fun getAvailableDependencies(): List<Dependency> {
        return listOf(
            // AndroidX
            Dependency(
                id = "androidx.core",
                group = "androidx.core",
                name = "core-ktx",
                version = "1.12.0",
                category = "AndroidX"
            ),
            Dependency(
                id = "androidx.appcompat",
                group = "androidx.appcompat",
                name = "appcompat",
                version = "1.6.1",
                category = "AndroidX"
            ),
            Dependency(
                id = "androidx.lifecycle",
                group = "androidx.lifecycle",
                name = "lifecycle-runtime-ktx",
                version = "2.6.2",
                category = "AndroidX"
            ),

            // Compose
            Dependency(
                id = "androidx.compose.ui",
                group = "androidx.compose.ui",
                name = "ui",
                version = "1.5.0",
                category = "Jetpack Compose"
            ),
            Dependency(
                id = "androidx.compose.material3",
                group = "androidx.compose.material3",
                name = "material3",
                version = "1.1.0",
                category = "Jetpack Compose"
            ),
            Dependency(
                id = "androidx.activity.compose",
                group = "androidx.activity",
                name = "activity-compose",
                version = "1.7.2",
                category = "Jetpack Compose"
            ),

            // Material Design
            Dependency(
                id = "com.google.android.material",
                group = "com.google.android.material",
                name = "material",
                version = "1.10.0",
                category = "Material Design"
            ),

            // Networking
            Dependency(
                id = "com.squareup.okhttp3",
                group = "com.squareup.okhttp3",
                name = "okhttp",
                version = "4.11.0",
                category = "Networking"
            ),
            Dependency(
                id = "com.squareup.retrofit2",
                group = "com.squareup.retrofit2",
                name = "retrofit",
                version = "2.9.0",
                category = "Networking"
            ),

            // JSON
            Dependency(
                id = "com.google.code.gson",
                group = "com.google.code.gson",
                name = "gson",
                version = "2.10.1",
                category = "JSON"
            ),

            // Database
            Dependency(
                id = "androidx.room",
                group = "androidx.room",
                name = "room-runtime",
                version = "2.5.2",
                category = "Database"
            ),

            // Testing
            Dependency(
                id = "junit",
                group = "junit",
                name = "junit",
                version = "4.13.2",
                category = "Testing"
            ),
            Dependency(
                id = "androidx.test.ext",
                group = "androidx.test.ext",
                name = "junit",
                version = "1.1.5",
                category = "Testing"
            )
        )
    }

    /**
     * Parse dependencies from build file
     */
    private fun parseDependencies(content: String): List<Dependency> {
        val dependencies = mutableListOf<Dependency>()
        val regex = Regex("""implementation\("([^:]+):([^:]+):([^"]+)"\)""")

        regex.findAll(content).forEach { match ->
            val (group, name, version) = match.destructured
            dependencies.add(
                Dependency(
                    id = "$group:$name",
                    group = group,
                    name = name,
                    version = version,
                    category = "Custom"
                )
            )
        }

        return dependencies
    }

    /**
     * Check for dependency conflicts
     */
    fun checkConflicts(): List<String> {
        val conflicts = mutableListOf<String>()
        val dependencies = getDependencies()

        // Check for duplicate groups
        val groupCounts = dependencies.groupingBy { it.group }.eachCount()
        groupCounts.forEach { (group, count) ->
            if (count > 1) {
                conflicts.add("Multiple versions of $group found")
            }
        }

        return conflicts
    }

    /**
     * Get dependency tree
     */
    fun getDependencyTree(): String {
        val dependencies = getDependencies()
        return buildString {
            append("Dependencies:\n")
            dependencies.forEach { dep ->
                append("├── ${dep.group}:${dep.name}:${dep.version}\n")
            }
        }
    }
}

/**
 * Dependency data class
 */
data class Dependency(
    val id: String,
    val group: String,
    val name: String,
    val version: String,
    val category: String = "Other"
) {
    fun getDisplayName(): String = "$group:$name"
    fun getFullName(): String = "$group:$name:$version"
}
