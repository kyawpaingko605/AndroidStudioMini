# Android Studio Mini - UI Structure

## Main Components

### 1. Top App Bar
- **Logo & Title**: Android Studio Mini branding
- **Action Buttons**:
  - New Project (+ icon)
  - Save (💾 icon)
  - Run (▶ icon, green)
  - Settings (⚙ icon)

### 2. Left Sidebar - Project Explorer (250dp)
- **Header**: "Project Explorer" with folder icon
- **Content**: Hierarchical project tree
  - MyProject (root)
    - app
      - src
        - main
          - java
            - MainActivity.kt
          - res
            - layout
            - values
          - AndroidManifest.xml
  - **Icons**:
    - 📁 Folders (yellow)
    - 📄 Files (gray)

### 3. Center - Code Editor (flexible)
- **Tab Bar**: File tabs with close buttons
  - MainActivity.kt (active)
  - activity_main.xml
  - AndroidManifest.xml
- **Line Numbers**: Left side (50dp width)
- **Code Area**: Monospace font, syntax highlighting
  - Keywords: Blue (#569CD6)
  - Strings: Orange (#CE9178)
  - Comments: Green (#6A9955)
  - Text: Light gray (#D4D4D4)

### 4. Right Sidebar - Inspector (200dp)
- **Properties Panel**:
  - File name
  - File size
  - Last modified
  - Additional metadata

### 5. Bottom Panel (200dp height)
- **Tab Navigation**:
  - Build
  - Logcat
  - Terminal
  - Problems
- **Content Area**:
  - **Build**: Compilation output
  - **Logcat**: System logs (color-coded)
  - **Terminal**: Command line interface
  - **Problems**: Error/warning list

## Color Scheme

| Element | Color | Hex |
|---------|-------|-----|
| Background | Dark Gray | #1E1E1E |
| Surface | Darker Gray | #252526 |
| Text | Light Gray | #D4D4D4 |
| Accent | Blue | #2196F3 |
| Success | Green | #4CAF50 |
| Warning | Orange | #FF9800 |
| Error | Red | #F44336 |
| Folder | Yellow | #FFC107 |
| Divider | Gray | #3E3E42 |

## Layout Hierarchy

```
Column (Full Screen)
├── TopAppBar
├── Row (Main Content)
│   ├── ProjectExplorerPanel (250dp)
│   ├── CodeEditorPanel (flexible)
│   └── InspectorPanel (200dp)
└── BottomPanel (200dp)
```

## Responsive Design

- **Phone (Portrait)**: Single column with collapsible panels
- **Tablet (Landscape)**: Full layout with all panels visible
- **Large Screens**: Optimized spacing and font sizes

## Interactions

- **Tab Switching**: Click on bottom tabs to switch panels
- **File Selection**: Click on files in project explorer
- **Editor Tabs**: Click to switch between open files
- **New Project**: FAB or menu button to create project
- **Run/Build**: Quick action buttons in top bar

## Dark Theme Implementation

All colors use Material Design 3 dark theme principles:
- High contrast for readability
- Reduced eye strain for extended coding sessions
- Professional IDE aesthetic
- Consistent with VS Code dark theme

## Next Steps

1. Integrate Sora-Editor for actual code editing
2. Add project file operations
3. Implement build system integration
4. Add logcat parsing and display
5. Create project templates
