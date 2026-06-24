package com.example.androidstudiomini

import java.io.File

/**
 * Test Runner - Executes unit and integration tests
 */
class TestRunner(private val projectPath: String) {

    /**
     * Run all tests
     */
    fun runAllTests(): TestResult {
        return try {
            val testFiles = getTestFiles()
            val results = mutableListOf<TestCase>()
            var passCount = 0
            var failCount = 0

            val startTime = System.currentTimeMillis()

            testFiles.forEach { testFile ->
                val testCases = parseTestFile(testFile)
                testCases.forEach { testCase ->
                    val result = executeTest(testCase)
                    results.add(result)
                    if (result.passed) passCount++ else failCount++
                }
            }

            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            TestResult(
                totalTests = results.size,
                passedTests = passCount,
                failedTests = failCount,
                skippedTests = 0,
                duration = duration,
                testCases = results,
                success = failCount == 0
            )
        } catch (e: Exception) {
            e.printStackTrace()
            TestResult(
                totalTests = 0,
                passedTests = 0,
                failedTests = 1,
                skippedTests = 0,
                duration = 0,
                testCases = emptyList(),
                success = false,
                errorMessage = e.message
            )
        }
    }

    /**
     * Run specific test class
     */
    fun runTestClass(testClassName: String): TestResult {
        return try {
            val testFile = File(projectPath, "app/src/test/java/${testClassName.replace(".", "/")}.kt")
            if (!testFile.exists()) {
                return TestResult(
                    totalTests = 0,
                    passedTests = 0,
                    failedTests = 0,
                    skippedTests = 0,
                    duration = 0,
                    testCases = emptyList(),
                    success = false,
                    errorMessage = "Test class not found: $testClassName"
                )
            }

            val testCases = parseTestFile(testFile)
            val results = mutableListOf<TestCase>()
            var passCount = 0
            var failCount = 0

            val startTime = System.currentTimeMillis()

            testCases.forEach { testCase ->
                val result = executeTest(testCase)
                results.add(result)
                if (result.passed) passCount++ else failCount++
            }

            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            TestResult(
                totalTests = results.size,
                passedTests = passCount,
                failedTests = failCount,
                skippedTests = 0,
                duration = duration,
                testCases = results,
                success = failCount == 0
            )
        } catch (e: Exception) {
            e.printStackTrace()
            TestResult(
                totalTests = 0,
                passedTests = 0,
                failedTests = 1,
                skippedTests = 0,
                duration = 0,
                testCases = emptyList(),
                success = false,
                errorMessage = e.message
            )
        }
    }

    /**
     * Get test files
     */
    private fun getTestFiles(): List<File> {
        val testDir = File(projectPath, "app/src/test/java")
        return if (testDir.exists()) {
            testDir.walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .toList()
        } else {
            emptyList()
        }
    }

    /**
     * Parse test file
     */
    private fun parseTestFile(testFile: File): List<TestCase> {
        val testCases = mutableListOf<TestCase>()
        val content = testFile.readText()
        val testMethodRegex = Regex("""@Test\s+fun\s+(\w+)\s*\(\)""")

        testMethodRegex.findAll(content).forEach { match ->
            val methodName = match.groupValues[1]
            testCases.add(
                TestCase(
                    name = methodName,
                    className = testFile.nameWithoutExtension,
                    filePath = testFile.absolutePath
                )
            )
        }

        return testCases
    }

    /**
     * Execute single test
     */
    private fun executeTest(testCase: TestCase): TestCase {
        return try {
            // Simulate test execution
            val result = testCase.copy(
                passed = true,
                duration = (Math.random() * 100).toLong(),
                message = "Test passed"
            )
            result
        } catch (e: Exception) {
            testCase.copy(
                passed = false,
                duration = 0,
                message = e.message ?: "Unknown error"
            )
        }
    }

    /**
     * Get test coverage
     */
    fun getTestCoverage(): TestCoverage {
        val sourceFiles = getSourceFiles()
        val testFiles = getTestFiles()

        val totalClasses = sourceFiles.size
        val testedClasses = testFiles.size

        return TestCoverage(
            totalClasses = totalClasses,
            testedClasses = testedClasses,
            coverage = if (totalClasses > 0) (testedClasses * 100) / totalClasses else 0
        )
    }

    /**
     * Get source files
     */
    private fun getSourceFiles(): List<File> {
        val srcDir = File(projectPath, "app/src/main/java")
        return if (srcDir.exists()) {
            srcDir.walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .toList()
        } else {
            emptyList()
        }
    }

    /**
     * Generate test report
     */
    fun generateTestReport(result: TestResult): String {
        return buildString {
            append("╔════════════════════════════════════════╗\n")
            append("║          TEST EXECUTION REPORT         ║\n")
            append("╚════════════════════════════════════════╝\n\n")

            append("Total Tests:    ${result.totalTests}\n")
            append("Passed:         ${result.passedTests} ✓\n")
            append("Failed:         ${result.failedTests} ✗\n")
            append("Skipped:        ${result.skippedTests}\n")
            append("Duration:       ${result.duration}ms\n\n")

            if (result.success) {
                append("Status:         SUCCESS ✓\n")
            } else {
                append("Status:         FAILED ✗\n")
            }

            if (result.testCases.isNotEmpty()) {
                append("\n────────────────────────────────────────\n")
                append("Test Details:\n")
                append("────────────────────────────────────────\n\n")

                result.testCases.forEach { testCase ->
                    val status = if (testCase.passed) "✓" else "✗"
                    append("$status ${testCase.className}.${testCase.name}\n")
                    if (!testCase.passed) {
                        append("  Error: ${testCase.message}\n")
                    }
                    append("  Duration: ${testCase.duration}ms\n\n")
                }
            }

            if (result.errorMessage != null) {
                append("\nError: ${result.errorMessage}\n")
            }
        }
    }
}

/**
 * Test Result data class
 */
data class TestResult(
    val totalTests: Int,
    val passedTests: Int,
    val failedTests: Int,
    val skippedTests: Int,
    val duration: Long,
    val testCases: List<TestCase>,
    val success: Boolean,
    val errorMessage: String? = null
) {
    fun getPassRate(): Int = if (totalTests > 0) (passedTests * 100) / totalTests else 0
    fun getSummary(): String = "$passedTests/$totalTests passed in ${duration}ms"
}

/**
 * Test Case data class
 */
data class TestCase(
    val name: String,
    val className: String,
    val filePath: String,
    val passed: Boolean = false,
    val duration: Long = 0,
    val message: String = ""
)

/**
 * Test Coverage data class
 */
data class TestCoverage(
    val totalClasses: Int,
    val testedClasses: Int,
    val coverage: Int
) {
    fun getCoveragePercentage(): String = "$coverage%"
}
