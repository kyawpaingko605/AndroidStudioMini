package com.example.androidstudiomini

import java.io.File

/**
 * Project Analyzer - Analyzes project structure and code quality
 */
class ProjectAnalyzer(private val projectPath: String) {

    /**
     * Analyze project
     */
    fun analyzeProject(): ProjectAnalysis {
        val startTime = System.currentTimeMillis()

        val projectStats = getProjectStatistics()
        val codeQuality = analyzeCodeQuality()
        val issues = findIssues()
        val recommendations = generateRecommendations(issues)

        val duration = System.currentTimeMillis() - startTime

        return ProjectAnalysis(
            projectStats = projectStats,
            codeQuality = codeQuality,
            issues = issues,
            recommendations = recommendations,
            duration = duration
        )
    }

    /**
     * Get project statistics
     */
    private fun getProjectStatistics(): ProjectStatistics {
        val srcDir = File(projectPath, "app/src/main/java")
        val testDir = File(projectPath, "app/src/test/java")
        val resDir = File(projectPath, "app/src/main/res")

        val sourceFiles = if (srcDir.exists()) {
            srcDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
        } else {
            emptyList()
        }

        val testFiles = if (testDir.exists()) {
            testDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
        } else {
            emptyList()
        }

        val resourceFiles = if (resDir.exists()) {
            resDir.walkTopDown().filter { it.isFile }.toList()
        } else {
            emptyList()
        }

        val totalLines = sourceFiles.sumOf { it.readLines().size }
        val totalTestLines = testFiles.sumOf { it.readLines().size }

        return ProjectStatistics(
            sourceFiles = sourceFiles.size,
            testFiles = testFiles.size,
            resourceFiles = resourceFiles.size,
            totalLines = totalLines,
            totalTestLines = totalTestLines,
            projectSize = getDirectorySize(File(projectPath))
        )
    }

    /**
     * Analyze code quality
     */
    private fun analyzeCodeQuality(): CodeQuality {
        val srcDir = File(projectPath, "app/src/main/java")
        val sourceFiles = if (srcDir.exists()) {
            srcDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
        } else {
            emptyList()
        }

        var complexityScore = 0
        var styleScore = 100
        var securityScore = 100

        sourceFiles.forEach { file ->
            val content = file.readText()

            // Analyze complexity
            val functionCount = content.split("fun ").size - 1
            complexityScore += functionCount * 5

            // Analyze style
            if (content.contains("TODO")) styleScore -= 5
            if (content.contains("FIXME")) styleScore -= 5
            if (content.contains("var ")) styleScore -= 2

            // Analyze security
            if (content.contains("hardcoded")) securityScore -= 10
            if (content.contains("password")) securityScore -= 15
            if (content.contains("secret")) securityScore -= 15
        }

        val maintainability = (100 - (complexityScore / sourceFiles.size.coerceAtLeast(1))) / 2
        val overall = (styleScore + securityScore + maintainability) / 3

        return CodeQuality(
            complexity = complexityScore / sourceFiles.size.coerceAtLeast(1),
            style = styleScore.coerceIn(0, 100),
            security = securityScore.coerceIn(0, 100),
            maintainability = maintainability.coerceIn(0, 100),
            overall = overall.coerceIn(0, 100)
        )
    }

    /**
     * Find issues
     */
    private fun findIssues(): List<ProjectIssue> {
        val issues = mutableListOf<ProjectIssue>()

        // Check for missing files
        val requiredFiles = listOf(
            "app/src/main/AndroidManifest.xml",
            "build.gradle.kts",
            "app/build.gradle.kts"
        )

        requiredFiles.forEach { file ->
            if (!File(projectPath, file).exists()) {
                issues.add(
                    ProjectIssue(
                        severity = IssueSeverity.ERROR,
                        title = "Missing file: $file",
                        description = "Required file not found in project",
                        file = file
                    )
                )
            }
        }

        // Check for code issues
        val srcDir = File(projectPath, "app/src/main/java")
        if (srcDir.exists()) {
            srcDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file ->
                val content = file.readText()

                if (content.contains("TODO")) {
                    issues.add(
                        ProjectIssue(
                            severity = IssueSeverity.WARNING,
                            title = "TODO found",
                            description = "Incomplete code marked with TODO",
                            file = file.name
                        )
                    )
                }

                if (content.contains("println(")) {
                    issues.add(
                        ProjectIssue(
                            severity = IssueSeverity.INFO,
                            title = "Debug print found",
                            description = "Use proper logging instead of println",
                            file = file.name
                        )
                    )
                }
            }
        }

        return issues
    }

    /**
     * Generate recommendations
     */
    private fun generateRecommendations(issues: List<ProjectIssue>): List<String> {
        val recommendations = mutableListOf<String>()

        val errorCount = issues.count { it.severity == IssueSeverity.ERROR }
        val warningCount = issues.count { it.severity == IssueSeverity.WARNING }

        if (errorCount > 0) {
            recommendations.add("Fix $errorCount error(s) before building")
        }

        if (warningCount > 0) {
            recommendations.add("Address $warningCount warning(s) for better code quality")
        }

        val srcDir = File(projectPath, "app/src/main/java")
        val testDir = File(projectPath, "app/src/test/java")

        val sourceFiles = if (srcDir.exists()) {
            srcDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList().size
        } else {
            0
        }

        val testFiles = if (testDir.exists()) {
            testDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList().size
        } else {
            0
        }

        if (sourceFiles > 0 && testFiles == 0) {
            recommendations.add("Add unit tests to improve code coverage")
        }

        if (sourceFiles > 100) {
            recommendations.add("Consider breaking down large modules into smaller components")
        }

        return recommendations
    }

    /**
     * Get directory size
     */
    private fun getDirectorySize(dir: File): Long {
        return try {
            dir.walkTopDown().sumOf { it.length() }
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Generate report
     */
    fun generateReport(analysis: ProjectAnalysis): String {
        return buildString {
            append("╔════════════════════════════════════════╗\n")
            append("║         PROJECT ANALYSIS REPORT        ║\n")
            append("╚════════════════════════════════════════╝\n\n")

            // Statistics
            append("PROJECT STATISTICS\n")
            append("──────────────────────────────────────\n")
            append("Source Files:       ${analysis.projectStats.sourceFiles}\n")
            append("Test Files:         ${analysis.projectStats.testFiles}\n")
            append("Resource Files:     ${analysis.projectStats.resourceFiles}\n")
            append("Total Lines:        ${analysis.projectStats.totalLines}\n")
            append("Test Lines:         ${analysis.projectStats.totalTestLines}\n")
            append("Project Size:       ${formatSize(analysis.projectStats.projectSize)}\n\n")

            // Code Quality
            append("CODE QUALITY METRICS\n")
            append("──────────────────────────────────────\n")
            append("Complexity:         ${analysis.codeQuality.complexity}/100\n")
            append("Style:              ${analysis.codeQuality.style}/100\n")
            append("Security:           ${analysis.codeQuality.security}/100\n")
            append("Maintainability:    ${analysis.codeQuality.maintainability}/100\n")
            append("Overall:            ${analysis.codeQuality.overall}/100\n\n")

            // Issues
            if (analysis.issues.isNotEmpty()) {
                append("ISSUES FOUND (${analysis.issues.size})\n")
                append("──────────────────────────────────────\n")
                analysis.issues.forEach { issue ->
                    val icon = when (issue.severity) {
                        IssueSeverity.ERROR -> "✗"
                        IssueSeverity.WARNING -> "⚠"
                        IssueSeverity.INFO -> "ℹ"
                    }\n                    append("$icon ${issue.title}\n")
                    append("  ${issue.description}\n")
                    append("  File: ${issue.file}\n\n")
                }
            } else {
                append("ISSUES FOUND\n")
                append("──────────────────────────────────────\n")
                append("No issues detected ✓\n\n")
            }

            // Recommendations
            if (analysis.recommendations.isNotEmpty()) {
                append("RECOMMENDATIONS\n")
                append("──────────────────────────────────────\n")
                analysis.recommendations.forEach { recommendation ->
                    append("• $recommendation\n")
                }
                append("\n")
            }

            append("Analysis completed in ${analysis.duration}ms\n")
        }
    }

    /**
     * Format size
     */
    private fun formatSize(size: Long): String {
        return when {
            size <= 0L -> "0 B"
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            else -> "${size / (1024 * 1024)} MB"
        }
    }
}

/**
 * Project Analysis data class
 */
data class ProjectAnalysis(
    val projectStats: ProjectStatistics,
    val codeQuality: CodeQuality,
    val issues: List<ProjectIssue>,
    val recommendations: List<String>,
    val duration: Long
)

/**
 * Project Statistics data class
 */
data class ProjectStatistics(
    val sourceFiles: Int,
    val testFiles: Int,
    val resourceFiles: Int,
    val totalLines: Int,
    val totalTestLines: Int,
    val projectSize: Long
)

/**
 * Code Quality data class
 */
data class CodeQuality(
    val complexity: Int,
    val style: Int,
    val security: Int,
    val maintainability: Int,
    val overall: Int
)

/**
 * Project Issue data class
 */
data class ProjectIssue(
    val severity: IssueSeverity,
    val title: String,
    val description: String,
    val file: String
)

/**
 * Issue Severity enum
 */
enum class IssueSeverity {
    ERROR, WARNING, INFO
}
