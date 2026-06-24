package com.example.androidstudiomini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidstudiomini.ui.BottomPanelsContainer
import com.example.androidstudiomini.ui.BuildProgressDialog
import com.example.androidstudiomini.ui.IDEScreen
import com.example.androidstudiomini.ui.IDETopAppBar
import com.example.androidstudiomini.ui.NewFileDialog
import com.example.androidstudiomini.ui.NewProjectDialog
import com.example.androidstudiomini.ui.SettingsDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.ui.graphics.Color(0xFF1E1E1E)
                ) {
                    // Create ViewModel
                    val viewModel: AppViewModel = viewModel(
                        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                return AppViewModel(this@MainActivity) as T
                            }
                        }
                    )

                    // Collect state
                    val state by viewModel.appState.collectAsState()

                    // Main IDE Screen
                    IDEScreen(
                        projectName = state.currentProject?.name ?: "No Project",
                        projectFiles = state.projectFiles,
                        buildLogs = state.buildLogs,
                        onNewProject = { viewModel.showNewProjectDialog() },
                        onSave = { viewModel.saveCurrentFile() },
                        onRun = { viewModel.buildProject() },
                        onSettings = { viewModel.showSettingsDialog() },
                        onFileSelected = { file -> viewModel.selectFile(file) },
                        codeEditorFactory = { createCodeEditor() }
                    )

                    // New Project Dialog
                    if (state.showNewProjectDialog) {
                        NewProjectDialog(
                            onDismiss = { viewModel.hideNewProjectDialog() },
                            onCreateProject = { projectName, packageName ->
                                viewModel.createProject(projectName, packageName)
                                viewModel.hideNewProjectDialog()
                            }
                        )
                    }

                    // New File Dialog
                    if (state.showNewFileDialog) {
                        NewFileDialog(
                            onDismiss = { viewModel.hideNewFileDialog() },
                            onCreateFile = { fileName, fileType ->
                                viewModel.createNewFile(fileName, fileType)
                                viewModel.hideNewFileDialog()
                            }
                        )
                    }

                    // Settings Dialog
                    if (state.showSettingsDialog) {
                        SettingsDialog(
                            onDismiss = { viewModel.hideSettingsDialog() }
                        )
                    }

                    // Build Progress Dialog
                    BuildProgressDialog(
                        isBuilding = state.isBuilding,
                        buildProgress = state.buildProgress,
                        currentTask = if (state.buildLogs.isNotEmpty()) {
                            state.buildLogs.last().message
                        } else {
                            "Building..."
                        }
                    )

                    // Error handling
                    if (state.errorMessage != null) {
                        // Show error toast or snackbar
                        // For now, just log it
                        println("Error: ${state.errorMessage}")
                    }
                }
            }
        }
    }

    /**
     * Create code editor instance
     */
    private fun createCodeEditor(): io.github.rosemoe.sora.editor.CodeEditor {
        return io.github.rosemoe.sora.editor.CodeEditor(this)
    }
}
