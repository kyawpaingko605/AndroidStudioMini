package com.example.androidstudiomini

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * App State data class
 */
data class AppState(
    val currentProject: Project? = null,
    val projectFiles: List<ProjectFile> = emptyList(),
    val openFiles: List<EditorTab> = emptyList(),
    val activeTabIndex: Int = 0,
    val buildLogs: List<LogEntry> = emptyList(),
    val isBuilding: Boolean = false,
    val buildProgress: Int = 0,
    val buildResult: BuildResult? = null,
    val selectedFile: ProjectFile? = null,
    val showNewProjectDialog: Boolean = false,
    val showNewFileDialog: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val errorMessage: String? = null
)

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
 * App ViewModel - Manages application state and business logic
 */
class AppViewModel(private val context: Context) : ViewModel() {

    private val projectManager = ProjectManager(context)
    private val fileManager = FileManager(context)
    private val buildManager = BuildManager(context)
    private val codeEditorManager = CodeEditorManager(context)

    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    /**
     * Create new project
     */
    fun createProject(projectName: String, packageName: String) {
        viewModelScope.launch {
            try {
                val project = projectManager.createProject(projectName, packageName)
                projectManager.openProject(project.path)
                _appState.value = _appState.value.copy(
                    currentProject = project,
                    projectFiles = project.files,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _appState.value = _appState.value.copy(
                    errorMessage = "Failed to create project: ${e.message}"
                )
            }
        }
    }

    /**
     * Open existing project
     */
    fun openProject(projectPath: String) {
        viewModelScope.launch {
            try {
                val project = projectManager.openProject(projectPath)
                if (project != null) {
                    _appState.value = _appState.value.copy(
                        currentProject = project,
                        projectFiles = project.files,
                        errorMessage = null
                    )
                } else {
                    _appState.value = _appState.value.copy(
                        errorMessage = "Failed to open project"
                    )
                }
            } catch (e: Exception) {
                _appState.value = _appState.value.copy(
                    errorMessage = "Error opening project: ${e.message}"
                )
            }
        }
    }

    /**
     * Select file and open in editor
     */
    fun selectFile(file: ProjectFile) {
        if (!file.isDirectory) {
            // Check if file is already open
            val existingTab = _appState.value.openFiles.indexOfFirst { it.filePath == file.path }

            if (existingTab >= 0) {
                // Switch to existing tab
                _appState.value = _appState.value.copy(activeTabIndex = existingTab)
            } else {
                // Open new file
                val newTab = EditorTab(
                    fileName = file.name,
                    filePath = file.path,
                    content = file.content,
                    language = getLanguageFromExtension(file.name)
                )

                val updatedFiles = _appState.value.openFiles + newTab
                _appState.value = _appState.value.copy(
                    openFiles = updatedFiles,
                    activeTabIndex = updatedFiles.size - 1,
                    selectedFile = file
                )
            }
        }

        _appState.value = _appState.value.copy(selectedFile = file)
    }

    /**
     * Close editor tab
     */
    fun closeTab(index: Int) {
        val updatedFiles = _appState.value.openFiles.filterIndexed { i, _ -> i != index }
        var newActiveIndex = _appState.value.activeTabIndex

        if (newActiveIndex >= updatedFiles.size && updatedFiles.isNotEmpty()) {
            newActiveIndex = updatedFiles.size - 1
        }

        _appState.value = _appState.value.copy(
            openFiles = updatedFiles,
            activeTabIndex = newActiveIndex
        )
    }

    /**
     * Switch active tab
     */
    fun switchTab(index: Int) {
        if (index >= 0 && index < _appState.value.openFiles.size) {
            _appState.value = _appState.value.copy(activeTabIndex = index)
        }
    }

    /**
     * Update file content
     */
    fun updateFileContent(filePath: String, content: String) {
        val updatedFiles = _appState.value.openFiles.map { tab ->
            if (tab.filePath == filePath) {
                tab.copy(content = content, isModified = true)
            } else {
                tab
            }
        }

        _appState.value = _appState.value.copy(openFiles = updatedFiles)
    }

    /**
     * Save current file
     */
    fun saveCurrentFile() {
        val currentTab = _appState.value.openFiles.getOrNull(_appState.value.activeTabIndex)
        if (currentTab != null) {
            saveFile(currentTab.filePath, currentTab.content)
        }
    }

    /**
     * Save file
     */
    fun saveFile(filePath: String, content: String) {
        viewModelScope.launch {
            try {
                val success = fileManager.writeFile(filePath, content)
                if (success) {
                    // Mark as saved
                    val updatedFiles = _appState.value.openFiles.map { tab ->
                        if (tab.filePath == filePath) {
                            tab.copy(isModified = false)
                        } else {
                            tab
                        }
                    }
                    _appState.value = _appState.value.copy(openFiles = updatedFiles)
                } else {
                    _appState.value = _appState.value.copy(
                        errorMessage = "Failed to save file"
                    )
                }
            } catch (e: Exception) {
                _appState.value = _appState.value.copy(
                    errorMessage = "Error saving file: ${e.message}"
                )
            }
        }
    }

    /**
     * Build project
     */
    fun buildProject() {
        val project = _appState.value.currentProject ?: return

        viewModelScope.launch {
            try {
                _appState.value = _appState.value.copy(
                    isBuilding = true,
                    buildProgress = 0,
                    buildLogs = emptyList()
                )

                // Save all open files before building
                _appState.value.openFiles.forEach { tab ->
                    if (tab.isModified) {
                        fileManager.writeFile(tab.filePath, tab.content)
                    }
                }

                // Run build
                val result = buildManager.build(project)

                _appState.value = _appState.value.copy(
                    isBuilding = false,
                    buildProgress = 100,
                    buildResult = result,
                    buildLogs = buildManager.getBuildLogs(),
                    errorMessage = if (result.success) null else result.message
                )
            } catch (e: Exception) {
                _appState.value = _appState.value.copy(
                    isBuilding = false,
                    errorMessage = "Build failed: ${e.message}"
                )
            }
        }
    }

    /**
     * Clean build
     */
    fun cleanBuild() {
        val project = _appState.value.currentProject ?: return

        viewModelScope.launch {
            try {
                buildManager.cleanBuild(project)
                _appState.value = _appState.value.copy(
                    buildLogs = emptyList(),
                    buildResult = null
                )
            } catch (e: Exception) {
                _appState.value = _appState.value.copy(
                    errorMessage = "Clean failed: ${e.message}"
                )
            }
        }
    }

    /**
     * Create new file
     */
    fun createNewFile(fileName: String, fileType: String) {
        val project = _appState.value.currentProject ?: return

        viewModelScope.launch {
            try {
                val filePath = "${project.path}/app/src/main/$fileName"
                val success = fileManager.createFileFromTemplate(filePath, fileType)

                if (success) {
                    // Reload project files
                    val updatedProject = projectManager.openProject(project.path)
                    if (updatedProject != null) {
                        _appState.value = _appState.value.copy(
                            currentProject = updatedProject,
                            projectFiles = updatedProject.files
                        )
                    }
                } else {
                    _appState.value = _appState.value.copy(
                        errorMessage = "Failed to create file"
                    )
                }
            } catch (e: Exception) {
                _appState.value = _appState.value.copy(
                    errorMessage = "Error creating file: ${e.message}"
                )
            }
        }
    }

    /**
     * Delete file
     */
    fun deleteFile(filePath: String) {
        viewModelScope.launch {
            try {
                val success = fileManager.deleteFile(filePath)
                if (success) {
                    // Close tab if open
                    val tabIndex = _appState.value.openFiles.indexOfFirst { it.filePath == filePath }
                    if (tabIndex >= 0) {
                        closeTab(tabIndex)
                    }

                    // Reload project files
                    val project = _appState.value.currentProject
                    if (project != null) {
                        val updatedProject = projectManager.openProject(project.path)
                        if (updatedProject != null) {
                            _appState.value = _appState.value.copy(
                                currentProject = updatedProject,
                                projectFiles = updatedProject.files
                            )
                        }
                    }
                } else {
                    _appState.value = _appState.value.copy(
                        errorMessage = "Failed to delete file"
                    )
                }
            } catch (e: Exception) {
                _appState.value = _appState.value.copy(
                    errorMessage = "Error deleting file: ${e.message}"
                )
            }
        }
    }

    /**
     * Show/hide dialogs
     */
    fun showNewProjectDialog() {
        _appState.value = _appState.value.copy(showNewProjectDialog = true)
    }

    fun hideNewProjectDialog() {
        _appState.value = _appState.value.copy(showNewProjectDialog = false)
    }

    fun showNewFileDialog() {
        _appState.value = _appState.value.copy(showNewFileDialog = true)
    }

    fun hideNewFileDialog() {
        _appState.value = _appState.value.copy(showNewFileDialog = false)
    }

    fun showSettingsDialog() {
        _appState.value = _appState.value.copy(showSettingsDialog = true)
    }

    fun hideSettingsDialog() {
        _appState.value = _appState.value.copy(showSettingsDialog = false)
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _appState.value = _appState.value.copy(errorMessage = null)
    }

    /**
     * Get all projects
     */
    fun getAllProjects(): List<Project> {
        return projectManager.getAllProjects()
    }

    /**
     * Get build logs as string
     */
    fun getBuildLogsAsString(): String {
        return buildManager.getBuildLogsAsString()
    }

    /**
     * Get last build result
     */
    fun getLastBuildResult(): BuildResult? {
        return buildManager.getLastBuildResult()
    }
}

/**
 * Get language from file extension
 */
private fun getLanguageFromExtension(filename: String): String {
    return when {
        filename.endsWith(".java") -> "java"
        filename.endsWith(".kt") -> "kotlin"
        filename.endsWith(".xml") -> "xml"
        filename.endsWith(".gradle") -> "gradle"
        filename.endsWith(".properties") -> "properties"
        filename.endsWith(".json") -> "json"
        filename.endsWith(".md") -> "markdown"
        else -> "java"
    }
}
