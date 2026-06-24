package com.example.androidstudiomini

import java.io.File

/**
 * Build Utilities - Helper functions for build process
 */
object BuildUtils {

    /**
     * Validate project structure
     */
    fun validateProjectStructure(projectPath: String): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        val projectDir = File(projectPath)
        if (!projectDir.exists()) {
            errors.add("Project directory does not exist")
            return ValidationResult(false, errors, warnings)
        }

        // Check for required files
        val buildGradle = File(projectDir, "build.gradle.kts")
        if (!buildGradle.exists()) {
            errors.add("build.gradle.kts not found")
        }

        val appBuildGradle = File(projectDir, "app/build.gradle.kts")
        if (!appBuildGradle.exists()) {
            errors.add("app/build.gradle.kts not found")
        }

        val manifest = File(projectDir, "app/src/main/AndroidManifest.xml")
        if (!manifest.exists()) {
            errors.add("AndroidManifest.xml not found")
        }

        // Check for source files
        val srcDir = File(projectDir, "app/src/main/java")
        if (!srcDir.exists() || srcDir.listFiles()?.isEmpty() != false) {
            warnings.add("No source files found")
        }

        // Check for resources
        val resDir = File(projectDir, "app/src/main/res")
        if (!resDir.exists()) {
            warnings.add("No resources directory found")
        }

        return ValidationResult(errors.isEmpty(), errors, warnings)
    }

    /**
     * Get source files
     */
    fun getSourceFiles(projectPath: String): List<File> {
        val srcDir = File(projectPath, "app/src/main/java")
        return if (srcDir.exists()) {
            srcDir.walkTopDown()
                .filter { it.isFile && (it.extension == "java" || it.extension == "kt") }
                .toList()
        } else {
            emptyList()
        }
    }

    /**
     * Get resource files
     */
    fun getResourceFiles(projectPath: String): List<File> {
        val resDir = File(projectPath, "app/src/main/res")
        return if (resDir.exists()) {
            resDir.walkTopDown()
                .filter { it.isFile }
                .toList()
        } else {
            emptyList()
        }
    }

    /**
     * Calculate build time estimate
     */
    fun estimateBuildTime(projectPath: String): Long {
        val sourceFiles = getSourceFiles(projectPath).size
        val resourceFiles = getResourceFiles(projectPath).size

        // Rough estimate: 100ms per source file + 50ms per resource
        return (sourceFiles * 100 + resourceFiles * 50).toLong()
    }

    /**
     * Get build cache size
     */
    fun getBuildCacheSize(projectPath: String): Long {
        val buildDir = File(projectPath, "build")
        return if (buildDir.exists()) {
            buildDir.walkTopDown().sumOf { it.length() }
        } else {
            0L
        }
    }

    /**
     * Clean build cache
     */
    fun cleanBuildCache(projectPath: String): Boolean {
        return try {
            val buildDir = File(projectPath, "build")
            if (buildDir.exists()) {
                buildDir.deleteRecursively()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get compilation errors from source
     */
    fun getCompilationErrors(projectPath: String): List<CompilationError> {
        val errors = mutableListOf<CompilationError>()
        val sourceFiles = getSourceFiles(projectPath)

        sourceFiles.forEach { file ->
            val content = file.readText()
            // Simple error detection (can be enhanced)
            if (content.contains("TODO")) {
                errors.add(
                    CompilationError(
                        file = file.name,
                        line = 0,
                        message = "TODO found in code",
                        severity = ErrorSeverity.WARNING
                    )
                )
            }
        }

        return errors
    }

    /**
     * Format build output
     */
    fun formatBuildOutput(logs: List<LogEntry>): String {
        return logs.joinToString("\n") { log ->
            "[${log.level.name}] ${log.message}"
        }
    }

    /**
     * Parse build errors
     */
    fun parseBuildErrors(output: String): List<String> {
        val errors = mutableListOf<String>()
        output.lines().forEach { line ->
            if (line.contains("error", ignoreCase = true)) {
                errors.add(line)
            }
        }
        return errors
    }

    /**
     * Check for missing dependencies
     */
    fun checkMissingDependencies(projectPath: String): List<String> {
        val missing = mutableListOf<String>()
        val buildGradle = File(projectPath, "app/build.gradle.kts")

        if (buildGradle.exists()) {
            val content = buildGradle.readText()

            // Check for common dependencies
            val requiredDeps = listOf(
                "androidx.core:core-ktx",
                "androidx.appcompat:appcompat",
                "androidx.compose.ui:ui"
            )

            requiredDeps.forEach { dep ->
                if (!content.contains(dep)) {
                    missing.add(dep)
                }
            }
        }

        return missing
    }

    /**
     * Get build summary
     */
    fun getBuildSummary(result: BuildResult): String {
        return buildString {
            append("Build Result: ")
            append(if (result.success) "SUCCESS" else "FAILED")
            append("\n")
            append("Time: ${result.buildTime}ms\n")
            append("Errors: ${result.errors.size}\n")
            append("Warnings: ${result.warnings.size}\n")
            if (result.apkPath != null) {
                append("APK: ${result.apkPath}\n")
            }
        }
    }
}

/**
 * Validation Result
 */
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)

/**
 * Compilation Error
 */
data class CompilationError(
    val file: String,
    val line: Int,
    val message: String,
    val severity: ErrorSeverity
)

/**
 * Error Severity
 */
enum class ErrorSeverity {
    INFO, WARNING, ERROR
}
