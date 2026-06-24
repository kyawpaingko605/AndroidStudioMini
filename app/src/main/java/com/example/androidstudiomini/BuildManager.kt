package com.example.androidstudiomini

import android.content.Context
import java.io.File
import java.io.IOException

/**
 * Build result data class
 */
data class BuildResult(
    val success: Boolean,
    val message: String,
    val apkPath: String? = null,
    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),
    val buildTime: Long = 0
)

/**
 * Build log entry
 */
data class LogEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val level: LogLevel,
    val message: String,
    val tag: String = "Build"
)

enum class LogLevel {
    INFO, WARNING, ERROR, DEBUG
}

/**
 * BuildManager handles the complete build pipeline
 * Java Source → javac → .class → d8 → .dex → APK
 */
class BuildManager(private val context: Context) {

    private val buildLogs = mutableListOf<LogEntry>()
    private val buildCacheDir = File(context.cacheDir, "build_cache")
    private val outputDir = File(context.getExternalFilesDir(null), "outputs")

    init {
        buildCacheDir.mkdirs()
        outputDir.mkdirs()
    }

    /**
     * Start build process
     */
    fun build(project: Project): BuildResult {
        val startTime = System.currentTimeMillis()
        buildLogs.clear()

        addLog("Starting build for project: ${project.name}", LogLevel.INFO)

        return try {
            // Step 1: Prepare build
            addLog("Preparing build environment...", LogLevel.INFO)
            prepareBuild(project)

            // Step 2: Compile resources
            addLog("Compiling resources...", LogLevel.INFO)
            compileResources(project)

            // Step 3: Compile Java/Kotlin
            addLog("Compiling Java/Kotlin source...", LogLevel.INFO)
            compileJavaSource(project)

            // Step 4: Convert to Dex
            addLog("Converting to DEX format...", LogLevel.INFO)
            convertToDex(project)

            // Step 5: Package APK
            addLog("Packaging APK...", LogLevel.INFO)
            val apkPath = packageApk(project)

            // Step 6: Sign APK
            addLog("Signing APK...", LogLevel.INFO)
            signApk(apkPath)

            val buildTime = System.currentTimeMillis() - startTime
            addLog("Build completed successfully in ${buildTime}ms", LogLevel.INFO)

            BuildResult(
                success = true,
                message = "Build successful",
                apkPath = apkPath,
                buildTime = buildTime
            )
        } catch (e: Exception) {
            val buildTime = System.currentTimeMillis() - startTime
            addLog("Build failed: ${e.message}", LogLevel.ERROR)
            BuildResult(
                success = false,
                message = "Build failed: ${e.message}",
                errors = listOf(e.message ?: "Unknown error"),
                buildTime = buildTime
            )
        }
    }

    /**
     * Prepare build environment
     */
    private fun prepareBuild(project: Project) {
        val buildDir = File(project.path, "build")
        if (buildDir.exists()) {
            buildDir.deleteRecursively()
        }
        buildDir.mkdirs()

        // Create build subdirectories
        File(buildDir, "intermediates/classes").mkdirs()
        File(buildDir, "intermediates/dex").mkdirs()
        File(buildDir, "intermediates/res").mkdirs()
        File(buildDir, "outputs/apk").mkdirs()
    }

    /**
     * Compile resources (simulate aapt2)
     */
    private fun compileResources(project: Project) {
        val resDir = File(project.path, "app/src/main/res")
        val compiledResDir = File(project.path, "build/intermediates/res")

        if (resDir.exists()) {
            resDir.walkTopDown().forEach { file ->
                if (file.isFile && file.extension in listOf("xml", "png", "jpg")) {
                    val relativePath = file.relativeTo(resDir)
                    val outputFile = File(compiledResDir, relativePath.path)
                    outputFile.parentFile?.mkdirs()
                    file.copyTo(outputFile, overwrite = true)
                    addLog("Compiled resource: ${relativePath.path}", LogLevel.DEBUG)
                }
            }
        }

        // Generate R.java
        generateRJava(project)
    }

    /**
     * Generate R.java (simplified)
     */
    private fun generateRJava(project: Project) {
        val rJavaContent = """
            package ${project.packageName};

            public final class R {
                public static final class string {
                    public static final int app_name = 0x7f010000;
                }
                
                public static final class layout {
                    public static final int activity_main = 0x7f020000;
                }
                
                public static final class drawable {
                    public static final int ic_launcher = 0x7f030000;
                }
            }
        """.trimIndent()

        val rJavaDir = File(project.path, "build/intermediates/classes/${project.packageName.replace(".", "/")}")
        rJavaDir.mkdirs()
        File(rJavaDir, "R.java").writeText(rJavaContent)
        addLog("Generated R.java", LogLevel.DEBUG)
    }

    /**
     * Compile Java/Kotlin source (simulate javac)
     */
    private fun compileJavaSource(project: Project) {
        val srcDir = File(project.path, "app/src/main/java")
        val classesDir = File(project.path, "build/intermediates/classes")

        if (srcDir.exists()) {
            srcDir.walkTopDown().forEach { file ->
                if (file.isFile && file.extension in listOf("java", "kt")) {
                    val relativePath = file.relativeTo(srcDir)
                    val classFileName = relativePath.path.replace(".java", ".class").replace(".kt", ".class")
                    val outputFile = File(classesDir, classFileName)
                    outputFile.parentFile?.mkdirs()
                    
                    // Simulate compilation by copying (in real scenario, use javac/kotlinc)
                    file.copyTo(outputFile, overwrite = true)
                    addLog("Compiled: ${relativePath.path}", LogLevel.DEBUG)
                }
            }
        }
    }

    /**
     * Convert .class files to .dex (simulate d8)
     */
    private fun convertToDex(project: Project) {
        val classesDir = File(project.path, "build/intermediates/classes")
        val dexDir = File(project.path, "build/intermediates/dex")

        if (classesDir.exists()) {
            classesDir.walkTopDown().forEach { file ->
                if (file.isFile && file.extension == "class") {
                    val relativePath = file.relativeTo(classesDir)
                    val dexFileName = relativePath.path.replace(".class", ".dex")
                    val outputFile = File(dexDir, dexFileName)
                    outputFile.parentFile?.mkdirs()
                    
                    // Simulate dex conversion
                    file.copyTo(outputFile, overwrite = true)
                    addLog("Dexed: ${relativePath.path}", LogLevel.DEBUG)
                }
            }
        }

        // Create classes.dex
        val classesDex = File(dexDir, "classes.dex")
        classesDex.writeText("DEX_PLACEHOLDER")
        addLog("Created classes.dex", LogLevel.DEBUG)
    }

    /**
     * Package APK (simulate apk packaging)
     */
    private fun packageApk(project: Project): String {
        val apkDir = File(project.path, "build/outputs/apk")
        val apkFile = File(apkDir, "${project.name}-unsigned.apk")

        // Create a simple APK structure
        val apkContent = """
            This is a simulated APK file for ${project.name}
            Package: ${project.packageName}
            Min SDK: ${project.minSdk}
            Target SDK: ${project.targetSdk}
        """.trimIndent()

        apkFile.writeText(apkContent)
        addLog("Created APK: ${apkFile.absolutePath}", LogLevel.INFO)

        return apkFile.absolutePath
    }

    /**
     * Sign APK (simulate apksigner)
     */
    private fun signApk(apkPath: String) {
        val unsignedApk = File(apkPath)
        val signedApk = File(apkPath.replace("-unsigned.apk", "-signed.apk"))

        if (unsignedApk.exists()) {
            unsignedApk.copyTo(signedApk, overwrite = true)
            addLog("Signed APK: ${signedApk.absolutePath}", LogLevel.INFO)
        }
    }

    /**
     * Add log entry
     */
    private fun addLog(message: String, level: LogLevel) {
        val logEntry = LogEntry(
            level = level,
            message = message
        )
        buildLogs.add(logEntry)
    }

    /**
     * Get build logs
     */
    fun getBuildLogs(): List<LogEntry> = buildLogs.toList()

    /**
     * Get build logs as formatted string
     */
    fun getBuildLogsAsString(): String {
        return buildLogs.joinToString("\n") { log ->
            "[${log.level.name}] ${log.message}"
        }
    }

    /**
     * Clean build
     */
    fun cleanBuild(project: Project) {
        val buildDir = File(project.path, "build")
        if (buildDir.exists()) {
            buildDir.deleteRecursively()
            addLog("Build directory cleaned", LogLevel.INFO)
        }
    }

    /**
     * Get build cache size
     */
    fun getBuildCacheSize(): Long {
        return buildCacheDir.walkTopDown().sumOf { it.length() }
    }

    /**
     * Clear build cache
     */
    fun clearBuildCache() {
        buildCacheDir.deleteRecursively()
        buildCacheDir.mkdirs()
        addLog("Build cache cleared", LogLevel.INFO)
    }

    /**
     * Check if build is in progress
     */
    private var isBuildInProgress = false

    fun isBuilding(): Boolean = isBuildInProgress

    /**
     * Get last build result
     */
    private var lastBuildResult: BuildResult? = null

    fun getLastBuildResult(): BuildResult? = lastBuildResult
}
