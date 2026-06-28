package com.example.androidstudiomini

import android.content.Context
import java.io.File
import java.io.FileOutputStream
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
    var content: String = "",
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
     * Create a new project using assets template
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

        projectPath.mkdirs()
        
        // ၁။ Assets template ထဲက ဖိုင်တွေကို ပထမဆုံး copy ကူးထည့်မယ်
        copyTemplateFromAssets(projectPath)
        
        // ၂။ ကူးပြီးသားဖိုင်တွေထဲက Dynamic package name တွေနဲ့ Project name တွေကို လိုက်ပြင်မယ်
        setupProjectPlaceholders(projectPath, projectName, packageName)

        val project = Project(
            name = projectName,
            path = projectPath.absolutePath,
            packageName = packageName,
            minSdk = minSdk,
            targetSdk = targetSdk
        )

        currentProject = project
        loadProjectFiles(project) // ဖိုင်စာရင်းကိုပါ တစ်ခါတည်း အလိုအလျောက် load ပေးခြင်း
        return project
    }

    /**
     * Assets ထဲက template folder ကို project folder ထဲ ကူးထည့်ခြင်း
     */
    private fun copyTemplateFromAssets(targetDir: File) {
        copyAssetFolder("templates/empty-project", targetDir.absolutePath)
    }

    private fun copyAssetFolder(assetDirPath: String, targetDirPath: String) {
        val assetManager = context.assets
        val assets = assetManager.list(assetDirPath) ?: return

        if (assets.isEmpty()) {
            // ဖိုင်ဖြစ်ပါက တိုက်ရိုက် copy ကူးမည်
            try {
                assetManager.open(assetDirPath).use { input ->
                    FileOutputStream(File(targetDirPath)).use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // ဖိုဒါဖြစ်ပါက ဖိုဒါအသစ်ဆောက်ပြီး ထပ်ဆင့်ပတ်မည် (Recursive)
            val targetDir = File(targetDirPath)
            if (!targetDir.exists()) targetDir.mkdirs()

            for (asset in assets) {
                copyAssetFolder("$assetDirPath/$asset", "$targetDirPath/$asset")
            }
        }
    }

    /**
     * ကူးထားတဲ့ Template ဖိုင်တွေထဲက နေရာတွေကို User ပေးလိုက်တဲ့ packageName အတိုင်း လိုက်ပြင်ပေးခြင်း
     */
    private fun setupProjectPlaceholders(projectPath: File, projectName: String, packageName: String) {
        // build.gradle.kts ပြင်ဆင်ခြင်း
        val gradleFile = File(projectPath, "build.gradle.kts")
        if (gradleFile.exists()) {
            var content = gradleFile.readText()
            content = content.replace("com.example.emptyproject", packageName)
            gradleFile.writeText(content)
        }

        // AndroidManifest.xml ပြင်ဆင်ခြင်း
        val manifestFile = File(projectPath, "app/src/main/AndroidManifest.xml")
        if (manifestFile.exists()) {
            var content = manifestFile.readText()
            content = content.replace("com.example.emptyproject", packageName)
            manifestFile.writeText(content)
        }

        // strings.xml ပြင်ဆင်ခြင်း
        val stringsFile = File(projectPath, "app/src/main/res/values/strings.xml")
        if (stringsFile.exists()) {
            var content = stringsFile.readText()
            content = content.replace("Empty Project", projectName)
            stringsFile.writeText(content)
        }

        // MainActivity.kt ကို သက်ဆိုင်ရာ package folder အမှန်ထဲ ရွှေ့ပြီး ပြင်ဆင်ခြင်း
        val oldActivityFile = File(projectPath, "app/src/main/java/MainActivity.kt")
        if (oldActivityFile.exists()) {
            var content = oldActivityFile.readText()
            content = content.replace("package com.example.emptyproject", "package $packageName")
            
            // Package လမ်းကြောင်းအတိုင်း Folder အသစ်ဆောက်ပြီး အဲဒီထဲ ရွှေ့ထည့်ခြင်း
            val newJavaDir = File(projectPath, "app/src/main/java/${packageName.replace(".", "/")}")
            newJavaDir.mkdirs()
            
            val newActivityFile = File(newJavaDir, "MainActivity.kt")
            newActivityFile.writeText(content)
            
            // အပြင်က ဖိုင်အဟောင်းကို ဖျက်ပစ်ခြင်း
            oldActivityFile.delete()
        }
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
        val appDir = File(project.path)
        if (appDir.exists()) {
            project.files.clear()
            loadFilesRecursive(appDir, project.files)
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
        val editableExtensions = listOf(".java", ".kt", ".xml", ".gradle", ".kts", ".properties", ".json", ".md")
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
