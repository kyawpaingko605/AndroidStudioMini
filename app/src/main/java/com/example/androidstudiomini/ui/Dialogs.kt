package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * New Project Dialog
 */
@Composable
fun NewProjectDialog(
    onDismiss: () -> Unit,
    onCreateProject: (projectName: String, packageName: String) -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    var packageName by remember { mutableStateOf("com.example.app") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF252526))
                .padding(24.dp)
                .width(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Create New Project",
                    color = Color(0xFFD4D4D4),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )

                // Project Name Input
                Column {
                    Text(
                        text = "Project Name",
                        color = Color(0xFF569CD6),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    BasicTextField(
                        value = projectName,
                        onValueChange = { projectName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E1E1E))
                            .padding(8.dp),
                        textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                            color = Color(0xFFD4D4D4),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }

                // Package Name Input
                Column {
                    Text(
                        text = "Package Name",
                        color = Color(0xFF569CD6),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    BasicTextField(
                        value = packageName,
                        onValueChange = { packageName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E1E1E))
                            .padding(8.dp),
                        textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                            color = Color(0xFFD4D4D4),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3E3E42)
                        )
                    ) {
                        Text("Cancel", color = Color(0xFFD4D4D4))
                    }

                    Button(
                        onClick = {
                            if (projectName.isNotEmpty() && packageName.isNotEmpty()) {
                                onCreateProject(projectName, packageName)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        )
                    ) {
                        Text("Create", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * New File Dialog
 */
@Composable
fun NewFileDialog(
    onDismiss: () -> Unit,
    onCreateFile: (fileName: String, fileType: String) -> Unit
) {
    var fileName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("java") }

    val fileTypes = listOf(
        "java" to "Java Class",
        "kotlin" to "Kotlin Class",
        "xml" to "XML Layout",
        "properties" to "Properties"
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF252526))
                .padding(24.dp)
                .width(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Create New File",
                    color = Color(0xFFD4D4D4),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )

                // File Name Input
                Column {
                    Text(
                        text = "File Name",
                        color = Color(0xFF569CD6),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    BasicTextField(
                        value = fileName,
                        onValueChange = { fileName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E1E1E))
                            .padding(8.dp),
                        textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                            color = Color(0xFFD4D4D4),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }

                // File Type Selection
                Column {
                    Text(
                        text = "File Type",
                        color = Color(0xFF569CD6),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fileTypes.forEach { (type, label) ->
                            Button(
                                onClick = { selectedType = type },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedType == type) Color(0xFF2196F3) else Color(0xFF3E3E42)
                                )
                            ) {
                                Text(label, fontSize = 10.sp, color = Color(0xFFD4D4D4))
                            }
                        }
                    }
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3E3E42)
                        )
                    ) {
                        Text("Cancel", color = Color(0xFFD4D4D4))
                    }

                    Button(
                        onClick = {
                            if (fileName.isNotEmpty()) {
                                onCreateFile(fileName, selectedType)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        )
                    ) {
                        Text("Create", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Build Progress Dialog
 */
@Composable
fun BuildProgressDialog(
    isBuilding: Boolean,
    buildProgress: Int = 0,
    currentTask: String = "Building..."
) {
    if (isBuilding) {
        Dialog(
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF252526))
                    .padding(24.dp)
                    .width(300.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Building Project",
                        color = Color(0xFFD4D4D4),
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Text(
                        text = currentTask,
                        color = Color(0xFF6A6A6A),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color(0xFF3E3E42))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(buildProgress / 100f)
                                .background(Color(0xFF2196F3))
                        )
                    }

                    Text(
                        text = "$buildProgress%",
                        color = Color(0xFF6A6A6A),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

/**
 * Settings Dialog
 */
@Composable
fun SettingsDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF252526))
                .padding(24.dp)
                .width(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Settings",
                        color = Color(0xFFD4D4D4),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFFD4D4D4),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Settings options
                SettingItem("Theme", "Dark")
                SettingItem("Font Size", "12sp")
                SettingItem("Tab Width", "4")
                SettingItem("Min SDK", "26")
                SettingItem("Target SDK", "34")

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}

/**
 * Setting Item
 */
@Composable
fun SettingItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color(0xFFD4D4D4),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )

        Text(
            text = value,
            color = Color(0xFF6A6A6A),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
