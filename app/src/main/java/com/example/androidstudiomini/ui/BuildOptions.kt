package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
 * Build Options data class
 */
data class BuildOptions(
    val minifyEnabled: Boolean = false,
    val debuggable: Boolean = true,
    val shrinkResources: Boolean = false,
    val multiDex: Boolean = false,
    val incremental: Boolean = true,
    val parallel: Boolean = true,
    val cacheEnabled: Boolean = true,
    val optimizeEnabled: Boolean = false
)

/**
 * Advanced Build Options Dialog
 */
@Composable
fun BuildOptionsDialog(
    onDismiss: () -> Unit,
    onApply: (BuildOptions) -> Unit,
    initialOptions: BuildOptions = BuildOptions()
) {
    var options by remember { mutableStateOf(initialOptions) }

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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Build Options",
                        color = Color(0xFFD4D4D4),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFFD4D4D4),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDismiss() }
                    )
                }

                // Options
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BuildOptionItem(
                        label = "Enable Minification",
                        description = "Shrink and obfuscate code",
                        checked = options.minifyEnabled,
                        onCheckedChange = { options = options.copy(minifyEnabled = it) }
                    )

                    BuildOptionItem(
                        label = "Debuggable",
                        description = "Enable debugging on device",
                        checked = options.debuggable,
                        onCheckedChange = { options = options.copy(debuggable = it) }
                    )

                    BuildOptionItem(
                        label = "Shrink Resources",
                        description = "Remove unused resources",
                        checked = options.shrinkResources,
                        onCheckedChange = { options = options.copy(shrinkResources = it) }
                    )

                    BuildOptionItem(
                        label = "Multi-Dex",
                        description = "Support for 65K+ methods",
                        checked = options.multiDex,
                        onCheckedChange = { options = options.copy(multiDex = it) }
                    )

                    BuildOptionItem(
                        label = "Incremental Build",
                        description = "Only rebuild changed files",
                        checked = options.incremental,
                        onCheckedChange = { options = options.copy(incremental = it) }
                    )

                    BuildOptionItem(
                        label = "Parallel Build",
                        description = "Use multiple threads",
                        checked = options.parallel,
                        onCheckedChange = { options = options.copy(parallel = it) }
                    )

                    BuildOptionItem(
                        label = "Build Cache",
                        description = "Cache build outputs",
                        checked = options.cacheEnabled,
                        onCheckedChange = { options = options.copy(cacheEnabled = it) }
                    )

                    BuildOptionItem(
                        label = "Optimization",
                        description = "Enable code optimization",
                        checked = options.optimizeEnabled,
                        onCheckedChange = { options = options.copy(optimizeEnabled = it) }
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { options = BuildOptions() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3E3E42)
                        )
                    ) {
                        Text("Reset", color = Color(0xFFD4D4D4), fontSize = 11.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3E3E42)
                        )
                    ) {
                        Text("Cancel", color = Color(0xFFD4D4D4), fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            onApply(options)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        )
                    ) {
                        Text("Apply", color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

/**
 * Build Option Item
 */
@Composable
fun BuildOptionItem(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .clickable { onCheckedChange(!checked) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF2196F3),
                uncheckedColor = Color(0xFF3E3E42),
                checkmarkColor = Color.White
            )
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = Color(0xFFD4D4D4),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = description,
                color = Color(0xFF6A6A6A),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

/**
 * Build Options Preview
 */
@Composable
fun BuildOptionsPreview() {
    BuildOptionsDialog(
        onDismiss = {},
        onApply = {}
    )
}
