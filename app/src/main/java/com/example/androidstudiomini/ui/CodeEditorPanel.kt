package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.editor.CodeEditor

/**
 * Code Editor Panel Component
 */
@Composable
fun CodeEditorPanel(
    openFiles: List<EditorTab>,
    activeTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onTabClosed: (Int) -> Unit,
    codeEditorFactory: () -> CodeEditor,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFF1E1E1E))
            .fillMaxHeight()
    ) {
        // Tab Bar
        EditorTabBar(
            tabs = openFiles,
            activeTabIndex = activeTabIndex,
            onTabSelected = onTabSelected,
            onTabClosed = onTabClosed
        )

        // Code Editor
        if (openFiles.isNotEmpty() && activeTabIndex < openFiles.size) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E))
            ) {
                AndroidView(
                    factory = { codeEditorFactory() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No file open",
                    color = Color(0xFF6A6A6A),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * Editor Tab data class
 */
data class EditorTab(
    val fileName: String,
    val filePath: String,
    val content: String,
    val isModified: Boolean = false,
    val language: String = "java"
)

/**
 * Editor Tab Bar
 */
@Composable
fun EditorTabBar(
    tabs: List<EditorTab>,
    activeTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onTabClosed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF252526))
            .height(40.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(tabs.size) { index ->
            EditorTabItem(
                tab = tabs[index],
                isActive = index == activeTabIndex,
                onSelected = { onTabSelected(index) },
                onClosed = { onTabClosed(index) }
            )
        }
    }
}

/**
 * Individual Editor Tab Item
 */
@Composable
fun EditorTabItem(
    tab: EditorTab,
    isActive: Boolean,
    onSelected: () -> Unit,
    onClosed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                if (isActive) Color(0xFF1E1E1E) else Color(0xFF252526)
            )
            .clickable { onSelected() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // File name
        Text(
            text = tab.fileName + if (tab.isModified) " •" else "",
            color = if (isActive) Color(0xFFD4D4D4) else Color(0xFF808080),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Close button
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = if (isActive) Color(0xFFD4D4D4) else Color(0xFF808080),
            modifier = Modifier
                .size(16.dp)
                .clickable { onClosed() }
        )
    }
}

/**
 * Code Editor Toolbar
 */
@Composable
fun CodeEditorToolbar(
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onFormat: () -> Unit,
    onFind: () -> Unit,
    onReplace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF252526))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditorToolbarButton("↶ Undo", onClick = onUndo)
        EditorToolbarButton("↷ Redo", onClick = onRedo)
        EditorToolbarButton("⎇ Format", onClick = onFormat)
        EditorToolbarButton("🔍 Find", onClick = onFind)
        EditorToolbarButton("↔ Replace", onClick = onReplace)
    }
}

/**
 * Editor Toolbar Button
 */
@Composable
fun EditorToolbarButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        color = Color(0xFFD4D4D4),
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        modifier = modifier
            .background(Color(0xFF3E3E42))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

/**
 * Code Editor Status Bar
 */
@Composable
fun CodeEditorStatusBar(
    lineCount: Int,
    charCount: Int,
    currentLine: Int,
    currentColumn: Int,
    language: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF252526))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Line $currentLine, Col $currentColumn",
            color = Color(0xFF6A6A6A),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lines: $lineCount",
                color = Color(0xFF6A6A6A),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = "Chars: $charCount",
                color = Color(0xFF6A6A6A),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = language.uppercase(),
                color = Color(0xFF569CD6),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

/**
 * Code Editor Panel Preview
 */
@Composable
fun CodeEditorPanelPreview() {
    val sampleTabs = listOf(
        EditorTab(
            fileName = "MainActivity.kt",
            filePath = "/app/src/main/java/MainActivity.kt",
            content = "class MainActivity : AppCompatActivity() {\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n        setContentView(R.layout.activity_main)\n    }\n}",
            isModified = false,
            language = "kotlin"
        ),
        EditorTab(
            fileName = "activity_main.xml",
            filePath = "/app/src/main/res/layout/activity_main.xml",
            content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n    android:layout_width=\"match_parent\"\n    android:layout_height=\"match_parent\"\n    android:orientation=\"vertical\">\n</LinearLayout>",
            isModified = true,
            language = "xml"
        )
    )

    Column {
        EditorTabBar(
            tabs = sampleTabs,
            activeTabIndex = 0,
            onTabSelected = {},
            onTabClosed = {}
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Code Editor View",
                color = Color(0xFF6A6A6A),
                fontSize = 16.sp
            )
        }
    }
}
