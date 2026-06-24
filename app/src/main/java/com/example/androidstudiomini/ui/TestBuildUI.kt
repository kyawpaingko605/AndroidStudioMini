package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * Build Progress Dialog
 */
@Composable
fun BuildProgressDialog(
    isBuilding: Boolean,
    buildProgress: Int,
    buildLogs: List<String>,
    onCancel: () -> Unit
) {
    if (isBuilding) {
        Dialog(
            onDismissRequest = onCancel
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF252526))
                    .padding(24.dp)
                    .width(500.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title
                    Text(
                        text = "Building Project...",
                        color = Color(0xFFD4D4D4),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    // Progress bar
                    LinearProgressIndicator(
                        progress = buildProgress / 100f,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF2196F3),
                        trackColor = Color(0xFF3E3E42)
                    )

                    // Progress text
                    Text(
                        text = "$buildProgress%",
                        color = Color(0xFF6A6A6A),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    // Build logs
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .background(Color(0xFF1E1E1E))
                            .verticalScroll(rememberScrollState())
                            .padding(8.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            buildLogs.forEach { log ->
                                Text(
                                    text = log,
                                    color = Color(0xFF6A6A6A),
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    // Cancel button
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3E3E42)
                        )
                    ) {
                        Text("Cancel", color = Color(0xFFD4D4D4))
                    }
                }
            }
        }
    }
}

/**
 * Test Results Dialog
 */
@Composable
fun TestResultsDialog(
    totalTests: Int,
    passedTests: Int,
    failedTests: Int,
    testLogs: List<String>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF252526))
                .padding(24.dp)
                .width(500.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Test Results",
                    color = Color(0xFFD4D4D4),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                // Results summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = totalTests.toString(),
                            color = Color(0xFFD4D4D4),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total",
                            color = Color(0xFF6A6A6A),
                            fontSize = 11.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = passedTests.toString(),
                            color = Color(0xFF4CAF50),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Passed",
                            color = Color(0xFF6A6A6A),
                            fontSize = 11.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = failedTests.toString(),
                            color = Color(0xFFF44336),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Failed",
                            color = Color(0xFF6A6A6A),
                            fontSize = 11.sp
                        )
                    }
                }

                // Test logs
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                        .background(Color(0xFF1E1E1E))
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        testLogs.forEach { log ->
                            val (icon, color) = when {
                                log.contains("✓") -> "✓" to Color(0xFF4CAF50)
                                log.contains("✗") -> "✗" to Color(0xFFF44336)
                                else -> "•" to Color(0xFF6A6A6A)
                            }

                            Text(
                                text = log,
                                color = color,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

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
 * Build Result Dialog
 */
@Composable
fun BuildResultDialog(
    success: Boolean,
    apkPath: String?,
    apkSize: String,
    duration: String,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onInstall: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF252526))
                .padding(24.dp)
                .width(450.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title and status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (success) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = if (success) "Success" else "Failed",
                        tint = if (success) Color(0xFF4CAF50) else Color(0xFFF44336),
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = if (success) "Build Successful" else "Build Failed",
                        color = if (success) Color(0xFF4CAF50) else Color(0xFFF44336),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Details
                if (success && apkPath != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E1E1E))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailRow("APK Path:", apkPath)
                        DetailRow("Size:", apkSize)
                        DetailRow("Duration:", duration)
                    }
                } else if (!success && errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E1E1E))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFF44336),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
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
                        Text("Close", color = Color(0xFFD4D4D4))
                    }

                    if (success) {
                        Button(
                            onClick = onInstall,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2196F3)
                            )
                        ) {
                            Text("Install", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Detail Row
 */
@Composable
fun DetailRow(label: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF6A6A6A),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )

        Text(
            text = value,
            color = Color(0xFFD4D4D4),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Project Analysis Dialog
 */
@Composable
fun ProjectAnalysisDialog(
    analysisReport: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF252526))
                .padding(24.dp)
                .width(550.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Project Analysis",
                    color = Color(0xFFD4D4D4),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                // Report
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .background(Color(0xFF1E1E1E))
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp)
                ) {
                    Text(
                        text = analysisReport,
                        color = Color(0xFF6A6A6A),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 12.sp
                    )
                }

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
