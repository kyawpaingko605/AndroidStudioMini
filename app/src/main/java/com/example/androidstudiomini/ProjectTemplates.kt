package com.example.androidstudiomini

import java.io.File

/**
 * Project Templates - Provides pre-built project templates
 */
object ProjectTemplates {

    /**
     * Get all available templates
     */
    fun getAvailableTemplates(): List<ProjectTemplate> {
        return listOf(
            getEmptyProjectTemplate(),
            getBasicActivityTemplate(),
            getComposableTemplate(),
            getLibraryTemplate(),
            getMultiModuleTemplate()
        )
    }

    /**
     * Empty Project Template
     */
    private fun getEmptyProjectTemplate(): ProjectTemplate {
        return ProjectTemplate(
            id = "empty",
            name = "Empty Project",
            description = "A minimal Android project with basic structure",
            icon = "📦",
            files = mapOf(
                "build.gradle.kts" to getBuildGradleContent(),
                "app/build.gradle.kts" to getAppBuildGradleContent(),
                "app/src/main/AndroidManifest.xml" to getManifestContent("com.example.app"),
                "app/src/main/java/com/example/app/MainActivity.kt" to getEmptyMainActivityContent(),
                "app/src/main/res/layout/activity_main.xml" to getEmptyLayoutContent(),
                "app/src/main/res/values/strings.xml" to getStringsContent(),
                "app/src/main/res/values/colors.xml" to getColorsContent(),
                "settings.gradle.kts" to getSettingsGradleContent()
            )
        )
    }

    /**
     * Basic Activity Template
     */
    private fun getBasicActivityTemplate(): ProjectTemplate {
        return ProjectTemplate(
            id = "basic_activity",
            name = "Basic Activity",
            description = "Project with a basic activity and layout",
            icon = "📱",
            files = mapOf(
                "build.gradle.kts" to getBuildGradleContent(),
                "app/build.gradle.kts" to getAppBuildGradleContent(),
                "app/src/main/AndroidManifest.xml" to getManifestContent("com.example.basicapp"),
                "app/src/main/java/com/example/basicapp/MainActivity.kt" to getBasicMainActivityContent(),
                "app/src/main/java/com/example/basicapp/SecondActivity.kt" to getSecondActivityContent(),
                "app/src/main/res/layout/activity_main.xml" to getBasicLayoutContent(),
                "app/src/main/res/layout/activity_second.xml" to getSecondActivityLayoutContent(),
                "app/src/main/res/values/strings.xml" to getStringsContent(),
                "app/src/main/res/values/colors.xml" to getColorsContent(),
                "settings.gradle.kts" to getSettingsGradleContent()
            )
        )
    }

    /**
     * Jetpack Compose Template
     */
    private fun getComposableTemplate(): ProjectTemplate {
        return ProjectTemplate(
            id = "compose",
            name = "Jetpack Compose",
            description = "Modern Android project with Jetpack Compose",
            icon = "🎨",
            files = mapOf(
                "build.gradle.kts" to getBuildGradleContent(),
                "app/build.gradle.kts" to getComposeBuildGradleContent(),
                "app/src/main/AndroidManifest.xml" to getManifestContent("com.example.compose"),
                "app/src/main/java/com/example/compose/MainActivity.kt" to getComposeMainActivityContent(),
                "app/src/main/java/com/example/compose/ui/theme/Theme.kt" to getThemeContent(),
                "app/src/main/java/com/example/compose/ui/screens/HomeScreen.kt" to getHomeScreenContent(),
                "app/src/main/res/values/strings.xml" to getStringsContent(),
                "app/src/main/res/values/colors.xml" to getColorsContent(),
                "settings.gradle.kts" to getSettingsGradleContent()
            )
        )
    }

    /**
     * Library Template
     */
    private fun getLibraryTemplate(): ProjectTemplate {
        return ProjectTemplate(
            id = "library",
            name = "Android Library",
            description = "Reusable Android library module",
            icon = "📚",
            files = mapOf(
                "build.gradle.kts" to getBuildGradleContent(),
                "lib/build.gradle.kts" to getLibraryBuildGradleContent(),
                "lib/src/main/AndroidManifest.xml" to getLibraryManifestContent(),
                "lib/src/main/java/com/example/lib/Utils.kt" to getUtilsContent(),
                "lib/src/main/java/com/example/lib/Helper.kt" to getHelperContent(),
                "lib/src/test/java/com/example/lib/UtilsTest.kt" to getUtilsTestContent(),
                "settings.gradle.kts" to getSettingsGradleContent()
            )
        )
    }

    /**
     * Multi-Module Template
     */
    private fun getMultiModuleTemplate(): ProjectTemplate {
        return ProjectTemplate(
            id = "multi_module",
            name = "Multi-Module Project",
            description = "Project with app and library modules",
            icon = "🏗️",
            files = mapOf(
                "build.gradle.kts" to getBuildGradleContent(),
                "app/build.gradle.kts" to getAppBuildGradleContent(),
                "lib/build.gradle.kts" to getLibraryBuildGradleContent(),
                "app/src/main/AndroidManifest.xml" to getManifestContent("com.example.multimodule"),
                "app/src/main/java/com/example/multimodule/MainActivity.kt" to getMultiModuleMainActivityContent(),
                "lib/src/main/AndroidManifest.xml" to getLibraryManifestContent(),
                "lib/src/main/java/com/example/lib/SharedUtils.kt" to getSharedUtilsContent(),
                "settings.gradle.kts" to getMultiModuleSettingsContent()
            )
        )
    }

    /**
     * Create project from template
     */
    fun createProjectFromTemplate(
        projectPath: String,
        packageName: String,
        template: ProjectTemplate
    ): Boolean {
        return try {
            template.files.forEach { (filePath, content) ->
                val adjustedPath = filePath.replace("com.example.app", packageName)
                val file = File(projectPath, adjustedPath)
                file.parentFile?.mkdirs()
                file.writeText(content)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ==================== Gradle Files ====================

    private fun getBuildGradleContent(): String = """
        plugins {
            id("com.android.application") version "8.1.0" apply false
            id("com.android.library") version "8.1.0" apply false
            id("org.jetbrains.kotlin.android") version "1.9.0" apply false
        }
    """.trimIndent()

    private fun getAppBuildGradleContent(): String = """
        plugins {
            id("com.android.application")
            id("kotlin-android")
        }

        android {
            compileSdk = 34
            defaultConfig {
                applicationId = "com.example.app"
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
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        dependencies {
            implementation("androidx.core:core-ktx:1.12.0")
            implementation("androidx.appcompat:appcompat:1.6.1")
            implementation("com.google.android.material:material:1.10.0")
            implementation("androidx.constraintlayout:constraintlayout:2.1.4")
            testImplementation("junit:junit:4.13.2")
            androidTestImplementation("androidx.test.ext:junit:1.1.5")
            androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        }
    """.trimIndent()

    private fun getComposeBuildGradleContent(): String = """
        plugins {
            id("com.android.application")
            id("kotlin-android")
        }

        android {
            compileSdk = 34
            defaultConfig {
                applicationId = "com.example.compose"
                minSdk = 26
                targetSdk = 34
                versionCode = 1
                versionName = "1.0"
            }
            buildFeatures {
                compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion = "1.5.0"
            }
            buildTypes {
                release {
                    isMinifyEnabled = false
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        dependencies {
            implementation("androidx.core:core-ktx:1.12.0")
            implementation("androidx.compose.ui:ui:1.5.0")
            implementation("androidx.compose.material3:material3:1.1.0")
            implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
            implementation("androidx.activity:activity-compose:1.7.2")
            testImplementation("junit:junit:4.13.2")
            androidTestImplementation("androidx.test.ext:junit:1.1.5")
            debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
        }
    """.trimIndent()

    private fun getLibraryBuildGradleContent(): String = """
        plugins {
            id("com.android.library")
            id("kotlin-android")
        }

        android {
            compileSdk = 34
            defaultConfig {
                minSdk = 26
                targetSdk = 34
            }
            buildTypes {
                release {
                    isMinifyEnabled = false
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        dependencies {
            implementation("androidx.core:core-ktx:1.12.0")
            testImplementation("junit:junit:4.13.2")
        }
    """.trimIndent()

    private fun getSettingsGradleContent(): String = """
        pluginManagement {
            repositories {
                google()
                mavenCentral()
                gradlePluginPortal()
            }
        }
        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                google()
                mavenCentral()
            }
        }
        rootProject.name = "MyApp"
        include(":app")
    """.trimIndent()

    private fun getMultiModuleSettingsContent(): String = """
        pluginManagement {
            repositories {
                google()
                mavenCentral()
                gradlePluginPortal()
            }
        }
        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                google()
                mavenCentral()
            }
        }
        rootProject.name = "MultiModuleApp"
        include(":app")
        include(":lib")
    """.trimIndent()

    // ==================== Manifest Files ====================

    private fun getManifestContent(packageName: String): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            package="$packageName">

            <application
                android:allowBackup="true"
                android:icon="@mipmap/ic_launcher"
                android:label="@string/app_name"
                android:roundIcon="@mipmap/ic_launcher_round"
                android:supportsRtl="true"
                android:theme="@style/Theme.AppCompat.Light.DarkActionBar">

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

    private fun getLibraryManifestContent(): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            package="com.example.lib" />
    """.trimIndent()

    // ==================== Kotlin Source Files ====================

    private fun getEmptyMainActivityContent(): String = """
        package com.example.app

        import androidx.appcompat.app.AppCompatActivity
        import android.os.Bundle

        class MainActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
            }
        }
    """.trimIndent()

    private fun getBasicMainActivityContent(): String = """
        package com.example.basicapp

        import androidx.appcompat.app.AppCompatActivity
        import android.os.Bundle
        import android.widget.Button
        import android.content.Intent

        class MainActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)

                val button = findViewById<Button>(R.id.button)
                button.setOnClickListener {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    """.trimIndent()

    private fun getSecondActivityContent(): String = """
        package com.example.basicapp

        import androidx.appcompat.app.AppCompatActivity
        import android.os.Bundle

        class SecondActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_second)
            }
        }
    """.trimIndent()

    private fun getComposeMainActivityContent(): String = """
        package com.example.compose

        import android.os.Bundle
        import androidx.activity.ComponentActivity
        import androidx.activity.compose.setContent
        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.material3.MaterialTheme
        import androidx.compose.material3.Surface
        import androidx.compose.material3.Text
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.tooling.preview.Preview
        import com.example.compose.ui.theme.ComposeTheme

        class MainActivity : ComponentActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContent {
                    ComposeTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Greeting("Android")
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

        @Preview(showBackground = true)
        @Composable
        fun GreetingPreview() {
            ComposeTheme {
                Greeting("Android")
            }
        }
    """.trimIndent()

    private fun getMultiModuleMainActivityContent(): String = """
        package com.example.multimodule

        import androidx.appcompat.app.AppCompatActivity
        import android.os.Bundle
        import android.widget.TextView
        import com.example.lib.SharedUtils

        class MainActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)

                val textView = findViewById<TextView>(R.id.textView)
                textView.text = SharedUtils.getGreeting()
            }
        }
    """.trimIndent()

    private fun getThemeContent(): String = """
        package com.example.compose.ui.theme

        import androidx.compose.material3.MaterialTheme
        import androidx.compose.material3.lightColorScheme
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.graphics.Color

        private val LightColorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            tertiary = Color(0xFF03DAC6)
        )

        @Composable
        fun ComposeTheme(content: @Composable () -> Unit) {
            MaterialTheme(
                colorScheme = LightColorScheme,
                content = content
            )
        }
    """.trimIndent()

    private fun getHomeScreenContent(): String = """
        package com.example.compose.ui.screens

        import androidx.compose.foundation.layout.Column
        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.foundation.layout.padding
        import androidx.compose.material3.Button
        import androidx.compose.material3.Text
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.mutableStateOf
        import androidx.compose.runtime.remember
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.unit.dp

        @Composable
        fun HomeScreen() {
            val count = remember { mutableStateOf(0) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Count: ${'$'}{count.value}")
                Button(onClick = { count.value++ }) {
                    Text("Increment")
                }
            }
        }
    """.trimIndent()

    private fun getUtilsContent(): String = """
        package com.example.lib

        object Utils {
            fun formatString(input: String): String {
                return input.trim().uppercase()
            }

            fun calculateSum(a: Int, b: Int): Int {
                return a + b
            }
        }
    """.trimIndent()

    private fun getHelperContent(): String = """
        package com.example.lib

        class Helper {
            fun isValidEmail(email: String): Boolean {
                return email.contains("@") && email.contains(".")
            }

            fun getInitials(name: String): String {
                return name.split(" ")
                    .mapNotNull { it.firstOrNull() }
                    .joinToString("")
                    .uppercase()
            }
        }
    """.trimIndent()

    private fun getSharedUtilsContent(): String = """
        package com.example.lib

        object SharedUtils {
            fun getGreeting(): String {
                return "Hello from shared library!"
            }

            fun getVersion(): String {
                return "1.0.0"
            }
        }
    """.trimIndent()

    private fun getUtilsTestContent(): String = """
        package com.example.lib

        import org.junit.Test
        import org.junit.Assert.*

        class UtilsTest {
            @Test
            fun testFormatString() {
                val result = Utils.formatString("  hello  ")
                assertEquals("HELLO", result)
            }

            @Test
            fun testCalculateSum() {
                val result = Utils.calculateSum(2, 3)
                assertEquals(5, result)
            }
        }
    """.trimIndent()

    // ==================== Layout Files ====================

    private fun getEmptyLayoutContent(): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/app_name"
                android:textSize="24sp" />
        </LinearLayout>
    """.trimIndent()

    private fun getBasicLayoutContent(): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Welcome to Android"
                android:textSize="24sp" />

            <Button
                android:id="@+id/button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Go to Second Activity"
                android:layout_marginTop="16dp" />
        </LinearLayout>
    """.trimIndent()

    private fun getSecondActivityLayoutContent(): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Second Activity"
                android:textSize="24sp" />
        </LinearLayout>
    """.trimIndent()

    // ==================== Resource Files ====================

    private fun getStringsContent(): String = """
        <resources>
            <string name="app_name">My Application</string>
        </resources>
    """.trimIndent()

    private fun getColorsContent(): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <color name="purple_200">#FFBB86FC</color>
            <color name="purple_500">#FF6200EE</color>
            <color name="purple_700">#FF3700B3</color>
            <color name="teal_200">#FF03DAC5</color>
            <color name="teal_700">#FF018786</color>
            <color name="black">#FF000000</color>
            <color name="white">#FFFFFFFF</color>
        </resources>
    """.trimIndent()
}

/**
 * Project Template data class
 */
data class ProjectTemplate(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val files: Map<String, String>
)
