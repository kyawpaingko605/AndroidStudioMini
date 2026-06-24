package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import com.example.androidstudiomini.ProjectTemplate

/**
 * Template Selector Dialog
 */
@Composable
fun TemplateSelectorDialog(
    templates: List<ProjectTemplate>,
    onDismiss: () -> Unit,
    onTemplateSelected: (ProjectTemplate) -> Unit
) {
    var selectedTemplate by remember { mutableStateOf<ProjectTemplate?>(null) }

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
                    text = "Select Project Template",
                    color = Color(0xFFD4D4D4),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                // Template List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(templates) { template ->
                        TemplateCard(
                            template = template,
                            isSelected = selectedTemplate?.id == template.id,
                            onSelected = { selectedTemplate = template }
                        )
                    }
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3E3E42)
                        )
                    ) {
                        Text("Cancel", color = Color(0xFFD4D4D4))
                    }

                    androidx.compose.material3.Button(
                        onClick = {
                            selectedTemplate?.let { onTemplateSelected(it) }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedTemplate != null,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            disabledContainerColor = Color(0xFF3E3E42)
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
 * Template Card
 */
@Composable
fun TemplateCard(
    template: ProjectTemplate,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isSelected) Color(0xFF3E3E42) else Color(0xFF1E1E1E)
            )
            .clickable { onSelected() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon
        Text(
            text = template.icon,
            fontSize = 32.sp,
            modifier = Modifier.width(40.dp)
        )

        // Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = template.name,
                color = Color(0xFFD4D4D4),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = template.description,
                color = Color(0xFF6A6A6A),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Selection indicator
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Template Preview
 */
@Composable
fun TemplateSelectorPreview() {
    val templates = listOf(
        ProjectTemplate(
            id = "empty",
            name = "Empty Project",
            description = "A minimal Android project with basic structure",
            icon = "📦",
            files = emptyMap()
        ),
        ProjectTemplate(
            id = "basic",
            name = "Basic Activity",
            description = "Project with a basic activity and layout",
            icon = "📱",
            files = emptyMap()
        ),
        ProjectTemplate(
            id = "compose",
            name = "Jetpack Compose",
            description = "Modern Android project with Jetpack Compose",
            icon = "🎨",
            files = emptyMap()
        )
    )

    TemplateSelectorDialog(
        templates = templates,
        onDismiss = {},
        onTemplateSelected = {}
    )
}
