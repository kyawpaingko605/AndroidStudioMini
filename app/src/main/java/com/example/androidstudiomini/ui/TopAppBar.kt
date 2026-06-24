package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Top App Bar Component
 */
@Composable
fun IDETopAppBar(
    projectName: String,
    onNewProject: () -> Unit,
    onSave: () -> Unit,
    onRun: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF252526))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side - Logo and Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Logo placeholder
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AS",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Title
            Column {
                Text(
                    text = "Android Studio Mini",
                    color = Color(0xFFD4D4D4),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                if (projectName.isNotEmpty()) {
                    Text(
                        text = projectName,
                        color = Color(0xFF6A6A6A),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Right side - Action buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppBarButton(
                icon = Icons.Default.Add,
                label = "New",
                onClick = onNewProject
            )

            AppBarButton(
                icon = Icons.Default.Save,
                label = "Save",
                onClick = onSave
            )

            AppBarButton(
                icon = Icons.Default.PlayArrow,
                label = "Run",
                onClick = onRun,
                tint = Color(0xFF4CAF50)
            )

            AppBarButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = onSettings
            )
        }
    }
}

/**
 * App Bar Button
 */
@Composable
fun AppBarButton(
    icon: androidx.compose.material.icons.Icons.Filled,
    label: String,
    onClick: () -> Unit,
    tint: Color = Color(0xFFD4D4D4),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color(0xFF3E3E42))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = label,
            color = Color(0xFFD4D4D4),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Top App Bar Preview
 */
@Composable
fun TopAppBarPreview() {
    IDETopAppBar(
        projectName = "MyApp",
        onNewProject = {},
        onSave = {},
        onRun = {},
        onSettings = {}
    )
}
