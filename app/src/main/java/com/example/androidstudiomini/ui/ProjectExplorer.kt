package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidstudiomini.ProjectFile

/**
 * Project Explorer Panel Component
 */
@Composable
fun ProjectExplorerPanel(
    projectFiles: List<ProjectFile>,
    onFileSelected: (ProjectFile) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(250.dp)
            .background(Color(0xFF252526))
            .fillMaxHeight()
    ) {
        // Header
        ProjectExplorerHeader()

        // File Tree
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(projectFiles) { file ->
                ProjectFileTreeItem(
                    file = file,
                    onFileSelected = onFileSelected,
                    level = 0
                )
            }
        }
    }
}

/**
 * Project Explorer Header
 */
@Composable
fun ProjectExplorerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Folder,
            contentDescription = "Project",
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Project Explorer",
            color = Color(0xFFD4D4D4),
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Project File Tree Item
 */
@Composable
fun ProjectFileTreeItem(
    file: ProjectFile,
    onFileSelected: (ProjectFile) -> Unit,
    level: Int,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // File item row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (file.isDirectory) {
                        isExpanded = !isExpanded
                    } else {
                        onFileSelected(file)
                    }
                }
                .padding(vertical = 4.dp)
                .padding(start = (level * 16).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Expand/Collapse icon for directories
            if (file.isDirectory) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF6A6A6A),
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Spacer(modifier = Modifier.width(4.dp))

            // File/Folder icon
            Icon(
                imageVector = if (file.isDirectory) {
                    if (isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder
                } else {
                    Icons.Default.InsertDriveFile
                },
                contentDescription = if (file.isDirectory) "Folder" else "File",
                tint = if (file.isDirectory) Color(0xFFFFC107) else Color(0xFF808080),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // File name
            Text(
                text = file.name,
                color = Color(0xFFD4D4D4),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.weight(1f)
            )
        }

        // Nested children
        if (file.isDirectory && isExpanded && file.children.isNotEmpty()) {
            file.children.forEach { childFile ->
                ProjectFileTreeItem(
                    file = childFile,
                    onFileSelected = onFileSelected,
                    level = level + 1
                )
            }
        }
    }
}

/**
 * File Tree Preview
 */
@Composable
fun ProjectExplorerPreview() {
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
                                                    content = "// Main Activity"
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
                        ),
                        ProjectFile(
                            name = "build.gradle.kts",
                            path = "/projects/MyProject/app/build.gradle.kts",
                            isDirectory = false
                        )
                    )
                )
            )
        )
    )

    ProjectExplorerPanel(
        projectFiles = sampleFiles,
        onFileSelected = { file ->
            println("Selected: ${file.name}")
        }
    )
}
