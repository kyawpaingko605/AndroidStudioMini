package com.example.androidstudiomini.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidstudiomini.LogEntry
import com.example.androidstudiomini.LogLevel

/**
 * Bottom Panels Container
 */
@Composable
fun BottomPanelsContainer(
    buildLogs: List<LogEntry>,
    logcatLogs: List<String>,
    problemsList: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(BottomPanelTab.BUILD) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFF1E1E1E))
    ) {
        // Tab Bar
        BottomPanelTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        // Content
        when (selectedTab) {
            BottomPanelTab.BUILD -> BuildPanel(logs = buildLogs)
            BottomPanelTab.LOGCAT -> LogcatPanel(logs = logcatLogs)
            BottomPanelTab.TERMINAL -> TerminalPanel()
            BottomPanelTab.PROBLEMS -> ProblemsPanel(problems = problemsList)
        }
    }
}

/**
 * Bottom Panel Tabs
 */
enum class BottomPanelTab(val displayName: String) {
    BUILD("Build"),
    LOGCAT("Logcat"),
    TERMINAL("Terminal"),
    PROBLEMS("Problems")
}

/**
 * Bottom Panel Tab Bar
 */
@Composable
fun BottomPanelTabBar(
    selectedTab: BottomPanelTab,
    onTabSelected: (BottomPanelTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF252526))
            .height(36.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BottomPanelTab.values().forEach { tab ->
            BottomPanelTabItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

/**
 * Bottom Panel Tab Item
 */
@Composable
fun BottomPanelTabItem(
    tab: BottomPanelTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                if (isSelected) Color(0xFF3E3E42) else Color(0xFF252526)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val icon = when (tab) {
            BottomPanelTab.BUILD -> Icons.Default.Build
            BottomPanelTab.LOGCAT -> Icons.Default.BugReport
            BottomPanelTab.TERMINAL -> Icons.Default.Terminal
            BottomPanelTab.PROBLEMS -> Icons.Default.Warning
        }

        Icon(
            imageVector = icon,
            contentDescription = tab.displayName,
            tint = if (isSelected) Color(0xFF2196F3) else Color(0xFF6A6A6A),
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = tab.displayName,
            color = if (isSelected) Color(0xFFD4D4D4) else Color(0xFF6A6A6A),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Build Panel
 */
@Composable
fun BuildPanel(
    logs: List<LogEntry>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        items(logs) { log ->
            BuildLogEntry(log)
        }

        if (logs.isEmpty()) {
            item {
                Text(
                    text = "No build logs yet",
                    color = Color(0xFF6A6A6A),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

/**
 * Build Log Entry
 */
@Composable
fun BuildLogEntry(
    log: LogEntry,
    modifier: Modifier = Modifier
) {
    val color = when (log.level) {
        LogLevel.INFO -> Color(0xFF6A9955)
        LogLevel.WARNING -> Color(0xFFFF9800)
        LogLevel.ERROR -> Color(0xFFF44336)
        LogLevel.DEBUG -> Color(0xFF6A6A6A)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "[${log.level.name}]",
            color = color,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(60.dp)
        )

        Text(
            text = log.message,
            color = Color(0xFFD4D4D4),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Logcat Panel
 */
@Composable
fun LogcatPanel(
    logs: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        items(logs) { log ->
            Text(
                text = log,
                color = Color(0xFFD4D4D4),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

        if (logs.isEmpty()) {
            item {
                Text(
                    text = "No logcat output",
                    color = Color(0xFF6A6A6A),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

/**
 * Terminal Panel
 */
@Composable
fun TerminalPanel(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        Text(
            text = "Terminal output will appear here",
            color = Color(0xFF6A6A6A),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Problems Panel
 */
@Composable
fun ProblemsPanel(
    problems: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        items(problems) { problem ->
            ProblemEntry(problem)
        }

        if (problems.isEmpty()) {
            item {
                Text(
                    text = "No problems detected",
                    color = Color(0xFF6A9955),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

/**
 * Problem Entry
 */
@Composable
fun ProblemEntry(
    problem: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = Color(0xFFF44336),
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = problem,
            color = Color(0xFFD4D4D4),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Bottom Panels Preview
 */
@Composable
fun BottomPanelsPreview() {
    val sampleLogs = listOf(
        LogEntry(
            level = LogLevel.INFO,
            message = "Starting build for project: MyApp"
        ),
        LogEntry(
            level = LogLevel.INFO,
            message = "Preparing build environment..."
        ),
        LogEntry(
            level = LogLevel.INFO,
            message = "Compiling resources..."
        ),
        LogEntry(
            level = LogLevel.DEBUG,
            message = "Compiled resource: layout/activity_main.xml"
        ),
        LogEntry(
            level = LogLevel.INFO,
            message = "Compiling Java/Kotlin source..."
        ),
        LogEntry(
            level = LogLevel.DEBUG,
            message = "Compiled: MainActivity.kt"
        ),
        LogEntry(
            level = LogLevel.INFO,
            message = "Build completed successfully in 2345ms"
        )
    )

    BottomPanelsContainer(
        buildLogs = sampleLogs,
        logcatLogs = emptyList(),
        problemsList = emptyList()
    )
}
