package com.example.androidstudiomini

import android.content.Context
import android.os.Build

/**
 * Device Simulator - Simulates device information and capabilities
 */
class DeviceSimulator(private val context: Context) {

    /**
     * Get device info
     */
    fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            device = Build.DEVICE,
            androidVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            hardware = Build.HARDWARE,
            product = Build.PRODUCT,
            brand = Build.BRAND,
            fingerprint = Build.FINGERPRINT
        )
    }

    /**
     * Get device capabilities
     */
    fun getDeviceCapabilities(): DeviceCapabilities {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory

        return DeviceCapabilities(
            totalMemory = totalMemory,
            freeMemory = freeMemory,
            usedMemory = usedMemory,
            processorCount = Runtime.getRuntime().availableProcessors(),
            maxMemory = runtime.maxMemory()
        )
    }

    /**
     * Get storage info
     */
    fun getStorageInfo(): StorageInfo {
        val appCacheDir = context.cacheDir
        val cacheSize = getDirectorySize(appCacheDir)
        val appFilesDir = context.filesDir
        val filesSize = getDirectorySize(appFilesDir)

        return StorageInfo(
            cacheSize = cacheSize,
            filesSize = filesSize,
            totalSize = cacheSize + filesSize
        )
    }

    /**
     * Get directory size
     */
    private fun getDirectorySize(dir: java.io.File): Long {
        return try {
            dir.walkTopDown().sumOf { it.length() }
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Check if device has sufficient resources for building
     */
    fun canBuild(): Boolean {
        val capabilities = getDeviceCapabilities()
        // Need at least 256MB free memory
        return capabilities.freeMemory > 256 * 1024 * 1024
    }

    /**
     * Get build recommendations
     */
    fun getBuildRecommendations(): List<String> {
        val recommendations = mutableListOf<String>()
        val capabilities = getDeviceCapabilities()

        if (capabilities.freeMemory < 512 * 1024 * 1024) {
            recommendations.add("Low memory available. Close other apps before building.")
        }

        if (capabilities.processorCount < 2) {
            recommendations.add("Single-core processor detected. Build may be slow.")
        }

        return recommendations
    }
}

/**
 * Device Info data class
 */
data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val device: String,
    val androidVersion: String,
    val sdkInt: Int,
    val hardware: String,
    val product: String,
    val brand: String,
    val fingerprint: String
) {
    fun getDisplayName(): String {
        return "$manufacturer $model (Android $androidVersion)"
    }
}

/**
 * Device Capabilities data class
 */
data class DeviceCapabilities(
    val totalMemory: Long,
    val freeMemory: Long,
    val usedMemory: Long,
    val processorCount: Int,
    val maxMemory: Long
) {
    fun getTotalMemoryMB(): Long = totalMemory / (1024 * 1024)
    fun getFreeMemoryMB(): Long = freeMemory / (1024 * 1024)
    fun getUsedMemoryMB(): Long = usedMemory / (1024 * 1024)
    fun getMaxMemoryMB(): Long = maxMemory / (1024 * 1024)
}

/**
 * Storage Info data class
 */
data class StorageInfo(
    val cacheSize: Long,
    val filesSize: Long,
    val totalSize: Long
) {
    fun getCacheSizeMB(): Long = cacheSize / (1024 * 1024)
    fun getFilesSizeMB(): Long = filesSize / (1024 * 1024)
    fun getTotalSizeMB(): Long = totalSize / (1024 * 1024)
}
