package com.example.androidstudiomini

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * APK Installer - Handles APK installation on device
 */
class APKInstaller(private val context: Context) {

    /**
     * Install APK
     */
    fun installAPK(apkPath: String): Boolean {
        return try {
            val apkFile = File(apkPath)
            if (!apkFile.exists()) {
                return false
            }

            val intent = Intent(Intent.ACTION_VIEW)
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    apkFile
                )
            } else {
                Uri.fromFile(apkFile)
            }

            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get APK info
     */
    fun getAPKInfo(apkPath: String): APKInfo? {
        return try {
            val apkFile = File(apkPath)
            if (!apkFile.exists()) {
                return null
            }

            APKInfo(
                name = apkFile.name,
                path = apkFile.absolutePath,
                size = apkFile.length(),
                lastModified = apkFile.lastModified()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Uninstall APK
     */
    fun uninstallAPK(packageName: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:$packageName")
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Check if package is installed
     */
    fun isPackageInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get installed packages
     */
    fun getInstalledPackages(): List<String> {
        return try {
            val packages = mutableListOf<String>()
            val pm = context.packageManager
            val apps = pm.getInstalledApplications(0)
            apps.forEach { app ->
                packages.add(app.packageName)
            }
            packages
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Launch app
     */
    fun launchApp(packageName: String): Boolean {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                context.startActivity(intent)
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

/**
 * APK Info data class
 */
data class APKInfo(
    val name: String,
    val path: String,
    val size: Long,
    val lastModified: Long
) {
    fun getSizeFormatted(): String {
        return when {
            size <= 0L -> "0 B"
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
            else -> "${size / (1024 * 1024 * 1024)} GB"
        }
    }
}
