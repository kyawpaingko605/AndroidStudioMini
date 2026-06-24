package com.example.androidstudiomini

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.IOException

/**
 * Project data class
 */
data class Project(
    val name: String,
    val path: String,
    val packageName: String,
    val minSdk: Int = 26,
    val targetSdk: Int = 34,
    val createdAt: Long = System.currentTimeMillis(),
    val files: MutableList<ProjectFile> = mutableListOf()
)

/**
 * Project file data class
 */
data class ProjectFile(
    val name: String,
    val path: String,
    val content: String = "",
    val isDirectory: Boolean = false,
    val children: MutableList<ProjectFile> = mutableListOf()
)

/**
 * ProjectManager handles all project-related operations
 */
class ProjectManager(private val context: Context) {

    private var currentProject: Project? = null
    private val projectsDir = File(context.getExternalFilesDir(null), "projects")

    init {
        if (!projectsDir.exists()) {
            projectsDir.mkdirs()
        }
    }

    /**
     * Create a new project
     */
    fun createProject(
        projectName: String,
        packageName: String,
        minSdk: Int = 26,
        targetSdk: Int = 34
    ): Project {
        val projectPath = File(projectsDir, projectName)
        
        if (projectPath.exists()) {
            throw IOException("Project already exists")
        }

        // Create project structure
        projectPath.mkdirs()
        createProjectStructure(projectPath, projectName, packageName)

        val project = Project(
            name = projectName,
            path = projectPath.absolutePath,
            packageName = packageName,
            minSdk = minSdk,
            targetSdk = targetSdk
        )

        currentProject = project
        return project
    }

    /**
     * Create standard Android project structure
     */
    private fun createProjectStructure(projectPath: File, projectName: String, packageName: String) {
        // Create directories
        val srcDir = File(projectPath, "app/src/main")
        val javaDir = File(srcDir, "java/${packageName.replace(".", "/")}")
        val resDir = File(srcDir, "res")
        val layoutDir = File(resDir, "layout")
        val valuesDir = File(resDir, "values")
        val drawableDir = File(resDir, "drawable")

        javaDir.mkdirs()
        layoutDir.mkdirs()
        valuesDir.mkdirs()
        drawableDir.mkdirs()

        // Create MainActivity.kt
        val mainActivityContent = """
            package $packageName

            import android.os.Bundle
            import androidx.activity.ComponentActivity
            import androidx.activity.compose.setContent
            import androidx.compose.foundation.layout.fillMaxSize
            import androidx.compose.material3.MaterialTheme
            import androidx.compose.material3.Surface
            import androidx.compose.material3.Text
            import androidx.compose.runtime.Composable
            import androidx.compose.ui.Alignment
            import androidx.compose.ui.Modifier

            class MainActivity : ComponentActivity() {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContent {
                        MaterialTheme {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                Greeting("$projectName")
                            }
                        }
                    }
                }
            }

            @Composable
            fun Greeting(name: String, modifier: Modifier = Modifier) {
                Text(
                    text = "Hello ${'$'}name!",
                    modifier = modifier
                )
            }
        """.trimIndent()

        File(javaDir, "MainActivity.kt").writeText(mainActivityContent)

        // Create AndroidManifest.xml
        val manifestContent = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                <application
                    android:allowBackup="true"
                    android:label="@string/app_name"
                    android:theme="@style/Theme.AppCompat">
                    <activity
                        android:name=".$packageName.MainActivity"
                        android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.MAIN" />
                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>
        """.trimIndent()

        File(srcDir, "AndroidManifest.xml").writeText(manifestContent)

        // Create strings.xml
        val stringsContent = """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <string name="app_name">$projectName</string>
            </resources>
        """.trimIndent()

        File(valuesDir, "strings.xml").writeText(stringsContent)

        // Create build.gradle.kts
        val buildGradleContent = """
            plugins {
                id("com.android.application")
                id("org.jetbrains.kotlin.android")
            }

            android {
                namespace = "$packageName"
                compileSdk = 34

                defaultConfig {
                    applicationId = "$packageName"
                    minSdk = 26
                    targetSdk = 34
                    versionCode = 1
                    versionName = "1.0"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                    }
                }
                
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                implementation("androidx.core:core-ktx:1.12.0")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
                implementation("androidx.activity:activity-compose:1.8.0")
                implementation("androidx.compose.ui:ui:1.5.0")
                implementation("androidx.compose.material3:material3:1.1.1")
            }
        """.trimIndent()

        File(projectPath, "build.gradle.kts").writeText(buildGradleContent)
    }

    /**
     * Open an existing project
     */
    fun openProject(projectPath: String): Project? {
        val projectFile = File(projectPath)
        
        if (!projectFile.exists() || !projectFile.isDirectory) {
            return null
        }

        val projectName = projectFile.name
        val packageName = readPackageNameFromManifest(projectFile)

        val project = Project(
            name = projectName,
            path = projectPath,
            packageName = packageName ?: "com.example.app"
        )

        currentProject = project
        loadProjectFiles(project)
        return project
    }

    /**
     * Read package name from AndroidManifest.xml
     */
    private fun readPackageNameFromManifest(projectPath: File): String? {
        val manifestFile = File(projectPath, "app/src/main/AndroidManifest.xml")
        if (manifestFile.exists()) {
            val content = manifestFile.readText()
            val regex = """package="([^"]+)"""".toRegex()
            val match = regex.find(content)
            return match?.groupValues?.get(1)
        }
        return null
    }

    /**
     * Load all project files
     */
    private fun loadProjectFiles(project: Project) {
        val srcDir = File(project.path, "app/src/main")
        if (srcDir.exists()) {
            project.files.clear()
            loadFilesRecursive(srcDir, project.files)
        }
    }

    /**
     * Recursively load files
     */
    private fun loadFilesRecursive(dir: File, fileList: MutableList<ProjectFile>) {
        dir.listFiles()?.forEach { file ->
            val projectFile = ProjectFile(
                name = file.name,
                path = file.absolutePath,
                isDirectory = file.isDirectory
            )

            if (file.isDirectory) {
                loadFilesRecursive(file, projectFile.children)
            } else if (file.isFile && isEditableFile(file.name)) {
                projectFile.content = file.readText()
            }

            fileList.add(projectFile)
        }
    }

    /**
     * Check if file is editable
     */
    private fun isEditableFile(filename: String): Boolean {
        val editableExtensions = listOf(".java", ".kt", ".xml", ".gradle", ".properties", ".json", ".md")
        return editableExtensions.any { filename.endsWith(it) }
    }

    /**
     * Get current project
     */
    fun getCurrentProject(): Project? = currentProject

    /**
     * Save file
     */
    fun saveFile(filePath: String, content: String): Boolean {
        return try {
            File(filePath).writeText(content)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Create new file
     */
    fun createFile(projectPath: String, fileName: String, content: String = ""): Boolean {
        return try {
            val file = File(projectPath, fileName)
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Delete file
     */
    fun deleteFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Rename file
     */
    fun renameFile(oldPath: String, newName: String): Boolean {
        return try {
            val oldFile = File(oldPath)
            val newFile = File(oldFile.parent, newName)
            oldFile.renameTo(newFile)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get all projects
     */
    fun getAllProjects(): List<Project> {
        val projects = mutableListOf<Project>()
        
        projectsDir.listFiles()?.forEach { projectDir ->
            if (projectDir.isDirectory) {
                val packageName = readPackageNameFromManifest(projectDir) ?: "com.example.app"
                projects.add(
                    Project(
                        name = projectDir.name,
                        path = projectDir.absolutePath,
                        packageName = packageName
                    )
                )
            }
        }

        return projects
    }

    /**
     * Delete project
     */
    fun deleteProject(projectName: String): Boolean {
        return try {
            val projectDir = File(projectsDir, projectName)
            projectDir.deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Export project as ZIP
     */
    fun exportProjectAsZip(projectPath: String, outputPath: String): Boolean {
        return try {
            val projectDir = File(projectPath)
            // TODO: Implement ZIP export
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Import project from ZIP
     */
    fun importProjectFromZip(zipPath: String): Boolean {
        return try {
            // TODO: Implement ZIP import
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
