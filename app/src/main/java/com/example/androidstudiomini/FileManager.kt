package com.example.androidstudiomini

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.IOException

/**
 * FileManager handles all file operations including:
 * - File creation, reading, writing, deletion
 * - Directory operations
 * - File search and filtering
 * - Storage Access Framework integration
 */
class FileManager(private val context: Context) {

    private val projectsDir = File(context.getExternalFilesDir(null), "projects")

    /**
     * Read file content
     */
    fun readFile(filePath: String): String? {
        return try {
            File(filePath).readText()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Write file content
     */
    fun writeFile(filePath: String, content: String): Boolean {
        return try {
            val file = File(filePath)
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Create directory
     */
    fun createDirectory(dirPath: String): Boolean {
        return try {
            File(dirPath).mkdirs()
        } catch (e: Exception) {
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
     * Delete directory recursively
     */
    fun deleteDirectory(dirPath: String): Boolean {
        return try {
            File(dirPath).deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Copy file
     */
    fun copyFile(sourcePath: String, destPath: String): Boolean {
        return try {
            val sourceFile = File(sourcePath)
            val destFile = File(destPath)
            destFile.parentFile?.mkdirs()
            sourceFile.copyTo(destFile, overwrite = true)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Move file
     */
    fun moveFile(sourcePath: String, destPath: String): Boolean {
        return try {
            val sourceFile = File(sourcePath)
            val destFile = File(destPath)
            destFile.parentFile?.mkdirs()
            sourceFile.renameTo(destFile)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Rename file
     */
    fun renameFile(filePath: String, newName: String): Boolean {
        return try {
            val file = File(filePath)
            val newFile = File(file.parent, newName)
            file.renameTo(newFile)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Check if file exists
     */
    fun fileExists(filePath: String): Boolean {
        return File(filePath).exists()
    }

    /**
     * Get file size
     */
    fun getFileSize(filePath: String): Long {
        return try {
            File(filePath).length()
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Get file size in human-readable format
     */
    fun getFileSizeFormatted(filePath: String): String {
        val bytes = getFileSize(filePath)
        return when {
            bytes <= 0L -> "0 B"
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }

    /**
     * Get last modified time
     */
    fun getLastModified(filePath: String): Long {
        return try {
            File(filePath).lastModified()
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * List files in directory
     */
    fun listFiles(dirPath: String): List<File> {
        return try {
            File(dirPath).listFiles()?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * List files recursively
     */
    fun listFilesRecursive(dirPath: String): List<File> {
        return try {
            File(dirPath).walkTopDown().toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Search files by name
     */
    fun searchFiles(dirPath: String, query: String): List<File> {
        return try {
            File(dirPath).walkTopDown()
                .filter { it.isFile && it.name.contains(query, ignoreCase = true) }
                .toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Search files by extension
     */
    fun searchFilesByExtension(dirPath: String, extension: String): List<File> {
        return try {
            File(dirPath).walkTopDown()
                .filter { it.isFile && it.extension == extension }
                .toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get file extension
     */
    fun getFileExtension(filePath: String): String {
        return File(filePath).extension
    }

    /**
     * Get file name without extension
     */
    fun getFileNameWithoutExtension(filePath: String): String {
        return File(filePath).nameWithoutExtension
    }

    /**
     * Get file name
     */
    fun getFileName(filePath: String): String {
        return File(filePath).name
    }

    /**
     * Get parent directory
     */
    fun getParentDirectory(filePath: String): String? {
        return File(filePath).parent
    }

    /**
     * Get absolute path
     */
    fun getAbsolutePath(filePath: String): String {
        return File(filePath).absolutePath
    }

    /**
     * Check if path is directory
     */
    fun isDirectory(path: String): Boolean {
        return File(path).isDirectory
    }

    /**
     * Check if path is file
     */
    fun isFile(path: String): Boolean {
        return File(path).isFile
    }

    /**
     * Create file from template
     */
    fun createFileFromTemplate(filePath: String, templateType: String): Boolean {
        val content = when (templateType) {
            "java_class" -> createJavaClassTemplate(getFileNameWithoutExtension(filePath))
            "kotlin_class" -> createKotlinClassTemplate(getFileNameWithoutExtension(filePath))
            "xml_layout" -> createXmlLayoutTemplate()
            "android_manifest" -> createAndroidManifestTemplate()
            else -> ""
        }
        return writeFile(filePath, content)
    }

    /**
     * Create Java class template
     */
    private fun createJavaClassTemplate(className: String): String {
        return """
            public class $className {
                
                public $className() {
                    // Constructor
                }
                
                public void doSomething() {
                    // TODO: Implement
                }
            }
        """.trimIndent()
    }

    /**
     * Create Kotlin class template
     */
    private fun createKotlinClassTemplate(className: String): String {
        return """
            class $className {
                
                fun doSomething() {
                    // TODO: Implement
                }
            }
        """.trimIndent()
    }

    /**
     * Create XML layout template
     */
    private fun createXmlLayoutTemplate(): String {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
                
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Hello World!" />
                    
            </LinearLayout>
        """.trimIndent()
    }

    /**
     * Create AndroidManifest.xml template
     */
    private fun createAndroidManifestTemplate(): String {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                
                <application
                    android:allowBackup="true"
                    android:label="@string/app_name">
                    
                    <activity
                        android:name=".MainActivity"
                        android:exported="true">
                        <intent-filter>
                            <action android:name="android.intent.action.MAIN" />
                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                    </activity>
                    
                </application>
                
            </manifest>
        """.trimIndent()
    }

    /**
     * Get directory size
     */
    fun getDirectorySize(dirPath: String): Long {
        return try {
            File(dirPath).walkTopDown().sumOf { it.length() }
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Get directory size in human-readable format
     */
    fun getDirectorySizeFormatted(dirPath: String): String {
        val bytes = getDirectorySize(dirPath)
        return when {
            bytes <= 0L -> "0 B"
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }

    /**
     * Get file count in directory
     */
    fun getFileCount(dirPath: String): Int {
        return try {
            File(dirPath).walkTopDown().count { it.isFile }
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Create backup of file
     */
    fun createBackup(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            val backupFile = File(file.parent, "${file.name}.backup")
            file.copyTo(backupFile, overwrite = true)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Restore from backup
     */
    fun restoreFromBackup(filePath: String): Boolean {
        return try {
            val backupFile = File("$filePath.backup")
            if (backupFile.exists()) {
                backupFile.copyTo(File(filePath), overwrite = true)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
