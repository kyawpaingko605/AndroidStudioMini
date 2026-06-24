package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidstudiomini.ProjectFile
import com.example.androidstudiomini.LogEntry
import io.github.rosemoe.sora.editor.CodeEditor

/**
 * Main IDE Screen
 */
@Composable
fun IDEScreen(
    projectName: String = "MyProject",
    projectFiles: List<ProjectFile> = emptyList(),
    buildLogs: List<LogEntry> = emptyList(),
    onNewProject: () -> Unit = {},
    onSave: () -> Unit = {},
    onRun: () -> Unit = {},
    onSettings: () -> Unit = {},
    onFileSelected: (ProjectFile) -> Unit = {},
    codeEditorFactory: () -> CodeEditor = { CodeEditor(null) }
) {
    var openFiles by remember { mutableStateOf<List<EditorTab>>(emptyList()) }
    var activeTabIndex by remember { mutableStateOf(0) }
    var selectedFile by remember { mutableStateOf<ProjectFile?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        // Top App Bar
        IDETopAppBar(
            projectName = projectName,
            onNewProject = onNewProject,
            onSave = {
                onSave()
            },
            onRun = {
                onRun()
            },
            onSettings = onSettings,
            modifier = Modifier.fillMaxWidth()
        )

        Divider()

        // Main Content Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Left Sidebar - Project Explorer
            ProjectExplorerPanel(
                projectFiles = projectFiles,
                onFileSelected = { file ->
                    selectedFile = file
                    onFileSelected(file)

                    // Add to open files if not already there
                    if (!file.isDirectory && !openFiles.any { it.filePath == file.path }) {
                        openFiles = openFiles + EditorTab(
                            fileName = file.name,
                            filePath = file.path,
                            content = file.content,
                            language = getLanguageFromFileName(file.name)
                        )
                        activeTabIndex = openFiles.size - 1
                    }
                },
                modifier = Modifier.fillMaxHeight()
            )

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color(0xFF3E3E42))
            )

            // Center - Code Editor
            CodeEditorPanel(
                openFiles = openFiles,
                activeTabIndex = activeTabIndex,
                onTabSelected = { activeTabIndex = it },
                onTabClosed = { index ->
                    openFiles = openFiles.filterIndexed { i, _ -> i != index }
                    if (activeTabIndex >= openFiles.size && openFiles.isNotEmpty()) {
                        activeTabIndex = openFiles.size - 1
                    }
                },
                codeEditorFactory = codeEditorFactory,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color(0xFF3E3E42))
            )

            // Right Sidebar - Inspector
            InspectorPanel(
                selectedFile = selectedFile,
                modifier = Modifier.fillMaxHeight()
            )
        }

        Divider()

        // Bottom Panels
        BottomPanelsContainer(
            buildLogs = buildLogs,
            logcatLogs = emptyList(),
            problemsList = emptyList(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Divider Component
 */
@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFF3E3E42))
    )
}

/**
 * Inspector Panel (Right Sidebar)
 */
@Composable
fun InspectorPanel(
    selectedFile: ProjectFile?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(200.dp)
            .background(Color(0xFF252526))
            .padding(12.dp)
    ) {
        Text(
            text = "Inspector",
            color = Color(0xFFD4D4D4),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedFile != null) {
            InspectorProperty("File", selectedFile.name)
            InspectorProperty("Path", selectedFile.path.takeLast(30))
            InspectorProperty("Type", if (selectedFile.isDirectory) "Folder" else "File")
            InspectorProperty("Size", "N/A")
        } else {
            Text(
                text = "No file selected",
                color = Color(0xFF6A6A6A),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

/**
 * Inspector Property
 */
@Composable
fun InspectorProperty(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF569CD6),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )

        Text(
            text = value,
            color = Color(0xFFD4D4D4),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Get language from file name
 */
fun getLanguageFromFileName(fileName: String): String {
    return when {
        fileName.endsWith(".java") -> "java"
        fileName.endsWith(".kt") -> "kotlin"
        fileName.endsWith(".xml") -> "xml"
        fileName.endsWith(".gradle") -> "gradle"
        fileName.endsWith(".properties") -> "properties"
        fileName.endsWith(".json") -> "json"
        fileName.endsWith(".md") -> "markdown"
        else -> "java"
    }
}

/**
 * IDE Screen Preview
 */
@Composable
fun IDEScreenPreview() {
    val sampleFiles = listOf(
        ProjectFile(
            name = "MyProject",
            path = "/projects/MyProject",
            isDirectory = true,
            children = mutableListOf(
                ProjectFile(
                    name = "app",
                    path = "/projects/MyProject/app",
                    isDirectory = true,
                    children = mutableListOf(
                        ProjectFile(
                            name = "src",
                            path = "/projects/MyProject/app/src",
                            isDirectory = true,
                            children = mutableListOf(
                                ProjectFile(
                                    name = "main",
                                    path = "/projects/MyProject/app/src/main",
                                    isDirectory = true,
                                    children = mutableListOf(
                                        ProjectFile(
                                            name = "java",
                                            path = "/projects/MyProject/app/src/main/java",
                                            isDirectory = true,
                                            children = mutableListOf(
                                                ProjectFile(
                                                    name = "MainActivity.kt",
                                                    path = "/projects/MyProject/app/src/main/java/MainActivity.kt",
                                                    isDirectory = false,
                                                    content = "class MainActivity : AppCompatActivity() {\n    // Code here\n}"
                                                )
                                            )
                                        ),
                                        ProjectFile(
                                            name = "AndroidManifest.xml",
                                            path = "/projects/MyProject/app/src/main/AndroidManifest.xml",
                                            isDirectory = false
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )

    val sampleLogs = listOf(
        LogEntry(level = com.example.androidstudiomini.LogLevel.INFO, message = "Build started"),
        LogEntry(level = com.example.androidstudiomini.LogLevel.INFO, message = "Compiling resources..."),
        LogEntry(level = com.example.androidstudiomini.LogLevel.DEBUG, message = "Compiled: MainActivity.kt"),
        LogEntry(level = com.example.androidstudiomini.LogLevel.INFO, message = "Build completed successfully")
    )

    IDEScreen(
        projectName = "MyProject",
        projectFiles = sampleFiles,
        buildLogs = sampleLogs
    )
}
