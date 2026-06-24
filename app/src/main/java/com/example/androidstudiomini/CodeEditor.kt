package com.example.androidstudiomini

import android.content.Context
import io.github.rosemoe.sora.editor.CodeEditor
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

/**
 * CodeEditorManager handles all code editor operations including:
 * - Syntax highlighting for Java, Kotlin, XML
 * - Auto-completion
 * - Error indicators
 * - Code folding
 */
class CodeEditorManager(private val context: Context) {

    private lateinit var codeEditor: CodeEditor
    private var currentLanguage: String = "java"

    /**
     * Initialize the code editor with TextMate language support
     */
    fun initializeEditor(codeEditor: CodeEditor) {
        this.codeEditor = codeEditor
        
        // Set up TextMate language registry
        try {
            // Initialize grammar registry
            val grammarRegistry = GrammarRegistry.getInstance()
            
            // Load language grammars
            loadLanguageGrammars(grammarRegistry)
            
            // Set editor color scheme (dark theme)
            setDarkTheme()
            
            // Configure editor options
            configureEditorOptions()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Load language grammars for Java, Kotlin, and XML
     */
    private fun loadLanguageGrammars(grammarRegistry: GrammarRegistry) {
        try {
            // Load Java grammar
            val javaGrammar = grammarRegistry.loadGrammar(
                FileProviderRegistry.getInstance().tryGetInputStream("java.json")
            )
            grammarRegistry.mapLanguageNameToGrammar("java", javaGrammar)
            
            // Load Kotlin grammar
            val kotlinGrammar = grammarRegistry.loadGrammar(
                FileProviderRegistry.getInstance().tryGetInputStream("kotlin.json")
            )
            grammarRegistry.mapLanguageNameToGrammar("kotlin", kotlinGrammar)
            
            // Load XML grammar
            val xmlGrammar = grammarRegistry.loadGrammar(
                FileProviderRegistry.getInstance().tryGetInputStream("xml.json")
            )
            grammarRegistry.mapLanguageNameToGrammar("xml", xmlGrammar)
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Set dark theme for the editor
     */
    private fun setDarkTheme() {
        val scheme = EditorColorScheme()
        
        // Background colors
        scheme.setColor(EditorColorScheme.WHOLE_BACKGROUND, 0xFF1E1E1E.toInt())
        scheme.setColor(EditorColorScheme.LINE_NUMBER_BACKGROUND, 0xFF1E1E1E.toInt())
        scheme.setColor(EditorColorScheme.LINE_NUMBER, 0xFF6A6A6A.toInt())
        
        // Text colors
        scheme.setColor(EditorColorScheme.TEXT_NORMAL, 0xFFD4D4D4.toInt())
        scheme.setColor(EditorColorScheme.TEXT_KEYWORD, 0xFF569CD6.toInt())
        scheme.setColor(EditorColorScheme.TEXT_BUILTIN, 0xFF4EC9B0.toInt())
        scheme.setColor(EditorColorScheme.TEXT_STRING, 0xFFCE9178.toInt())
        scheme.setColor(EditorColorScheme.TEXT_COMMENT, 0xFF6A9955.toInt())
        scheme.setColor(EditorColorScheme.TEXT_OPERATOR, 0xFFD4D4D4.toInt())
        
        // Selection and cursor
        scheme.setColor(EditorColorScheme.SELECTION_INSERT, 0xFF264F78.toInt())
        scheme.setColor(EditorColorScheme.SELECTION_HANDLE, 0xFF569CD6.toInt())
        scheme.setColor(EditorColorScheme.CURSOR_INSERT, 0xFFAEAFAD.toInt())
        
        // Error and warning
        scheme.setColor(EditorColorScheme.PROBLEM_ERROR, 0xFFF44336.toInt())
        scheme.setColor(EditorColorScheme.PROBLEM_WARNING, 0xFFFF9800.toInt())
        
        codeEditor.colorScheme = scheme
    }

    /**
     * Configure editor options
     */
    private fun configureEditorOptions() {
        codeEditor.apply {
            // Enable line numbers
            isLineNumberEnabled = true
            
            // Enable word wrap
            isWordwrap = false
            
            // Enable auto indent
            isAutoIndentEnabled = true
            
            // Enable auto completion
            isAutoCompletionEnabled = true
            
            // Set tab size
            tabWidth = 4
            
            // Enable highlight brackets
            isHighlightBracketPair = true
            
            // Enable highlight current line
            isHighlightCurrentLine = true
            
            // Enable highlight current block
            isHighlightCurrentBlock = true
        }
    }

    /**
     * Set language for syntax highlighting
     */
    fun setLanguage(language: String) {
        currentLanguage = language
        try {
            val textMateLanguage = TextMateLanguage.create(
                language,
                true
            )
            codeEditor.editorLanguage = textMateLanguage
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Load code content into editor
     */
    fun loadCode(code: String) {
        codeEditor.text = Content(code)
    }

    /**
     * Get current code content
     */
    fun getCode(): String {
        return codeEditor.text.toString()
    }

    /**
     * Clear editor content
     */
    fun clear() {
        codeEditor.text = Content("")
    }

    /**
     * Undo last action
     */
    fun undo() {
        if (codeEditor.canUndo()) {
            codeEditor.undo()
        }
    }

    /**
     * Redo last action
     */
    fun redo() {
        if (codeEditor.canRedo()) {
            codeEditor.redo()
        }
    }

    /**
     * Find text in editor
     */
    fun findText(query: String): Int {
        return codeEditor.text.indexOf(query)
    }

    /**
     * Replace text in editor
     */
    fun replaceText(oldText: String, newText: String) {
        val content = codeEditor.text.toString()
        codeEditor.text = Content(content.replace(oldText, newText))
    }

    /**
     * Get current cursor position
     */
    fun getCursorPosition(): Pair<Int, Int> {
        val line = codeEditor.cursor.leftLine
        val column = codeEditor.cursor.leftColumn
        return Pair(line, column)
    }

    /**
     * Set cursor position
     */
    fun setCursorPosition(line: Int, column: Int) {
        codeEditor.setSelection(line, column)
    }

    /**
     * Get selected text
     */
    fun getSelectedText(): String {
        return codeEditor.selectedText
    }

    /**
     * Format code (basic indentation)
     */
    fun formatCode() {
        val content = codeEditor.text.toString()
        val formatted = formatCodeContent(content)
        codeEditor.text = Content(formatted)
    }

    /**
     * Basic code formatting
     */
    private fun formatCodeContent(content: String): String {
        var indentLevel = 0
        val lines = content.split("\n")
        val formatted = StringBuilder()

        for (line in lines) {
            val trimmed = line.trim()
            
            // Decrease indent for closing braces
            if (trimmed.startsWith("}")) {
                indentLevel = maxOf(0, indentLevel - 1)
            }
            
            // Add indentation
            formatted.append("    ".repeat(indentLevel))
            formatted.append(trimmed)
            formatted.append("\n")
            
            // Increase indent for opening braces
            if (trimmed.endsWith("{")) {
                indentLevel++
            }
        }

        return formatted.toString()
    }

    /**
     * Get line count
     */
    fun getLineCount(): Int {
        return codeEditor.lineCount
    }

    /**
     * Get character count
     */
    fun getCharCount(): Int {
        return codeEditor.text.length
    }

    /**
     * Check if editor has unsaved changes
     */
    fun hasUnsavedChanges(): Boolean {
        return codeEditor.isModified
    }

    /**
     * Mark as saved
     */
    fun markAsSaved() {
        codeEditor.isModified = false
    }
}

/**
 * Syntax highlighting color constants
 */
object SyntaxColors {
    const val KEYWORD = 0xFF569CD6.toInt()
    const val STRING = 0xFFCE9178.toInt()
    const val COMMENT = 0xFF6A9955.toInt()
    const val NUMBER = 0xFFB5CEA8.toInt()
    const val FUNCTION = 0xFFDCDCAA.toInt()
    const val CLASS = 0xFF4EC9B0.toInt()
    const val OPERATOR = 0xFFD4D4D4.toInt()
    const val ERROR = 0xFFF44336.toInt()
    const val WARNING = 0xFFFF9800.toInt()
}

/**
 * Supported languages
 */
enum class SupportedLanguage(val displayName: String, val extension: String) {
    JAVA("Java", ".java"),
    KOTLIN("Kotlin", ".kt"),
    XML("XML", ".xml"),
    GRADLE("Gradle", ".gradle"),
    PROPERTIES("Properties", ".properties"),
    JSON("JSON", ".json"),
    MARKDOWN("Markdown", ".md")
}

/**
 * Get language from file extension
 */
fun getLanguageFromExtension(filename: String): SupportedLanguage {
    return when {
        filename.endsWith(".java") -> SupportedLanguage.JAVA
        filename.endsWith(".kt") -> SupportedLanguage.KOTLIN
        filename.endsWith(".xml") -> SupportedLanguage.XML
        filename.endsWith(".gradle") -> SupportedLanguage.GRADLE
        filename.endsWith(".properties") -> SupportedLanguage.PROPERTIES
        filename.endsWith(".json") -> SupportedLanguage.JSON
        filename.endsWith(".md") -> SupportedLanguage.MARKDOWN
        else -> SupportedLanguage.JAVA
    }
}
