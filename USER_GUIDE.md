# Android Studio Mini IDE - အသုံးပြုမည့်လမ်းညွှန်

## မိတ်ဆက်

**Android Studio Mini** သည် Android ဖုန်းတွင် အလုံးစုံ IDE (Integrated Development Environment) အဖြစ် အလုပ်လုပ်သည်။ ၎င်းကို အသုံးပြု၍ Java/Kotlin ကုဒ်ရေးသား၊ ပြင်ဆင်၊ ဖန်တီး၊ စမ်းသပ်နိုင်သည်။

---

## အခြေခံအသုံးပြုခြင်း

### 1. အပ်လီကေးရှင်းကို ထည့်သွင်းခြင်း

#### Step 1: APK ဖိုင်ကို ဒေါင်းလုဒ်လုပ်ခြင်း
```
APK ဖိုင်: AndroidStudioMini-debug-20260624_093412.apk
အရွယ်: 25-30 MB
```

#### Step 2: Android ဖုန်းတွင် ထည့်သွင်းခြင်း

**နည်းလမ်း A: ADB (Android Debug Bridge) ကို အသုံးပြုခြင်း**
```bash
adb install AndroidStudioMini-debug-20260624_093412.apk
```

**နည်းလမ်း B: ဖုန်းတွင် အလုံးလုံ ထည့်သွင်းခြင်း**
1. Settings → Security → Unknown Sources ကို ဖွင့်ခြင်း
2. APK ဖိုင်ကို ဖုန်းသို့ ကူးယူခြင်း
3. ဖိုင်မန်ဆာဖြင့် APK ဖိုင်ကို ထိခြင်း
4. "Install" ခလုတ်ကို နှိပ်ခြင်း

#### Step 3: အပ်လီကေးရှင်းကို စတင်ခြင်း
```
Home Screen သို့ ပြန်သွားခြင်း
Android Studio Mini အိုင်ကွန်ကို ရှာခြင်း
အိုင်ကွန်ကို နှိပ်ခြင်း
```

---

## IDE ကို အသုံးပြုခြင်း

### 2. အဓိက အင်္ဂါရပ်များ

IDE သည် ၅ ခုအဓိကအစိတ်အပိုင်းဖြင့် ဖွဲ့စည်းထားသည်:

```
┌─────────────────────────────────────────────┐
│          Top App Bar (Header)               │
│  Logo | New | Save | Run | Settings        │
├──────────────┬──────────────┬───────────────┤
│              │              │               │
│   Project    │   Code       │   Inspector   │
│   Explorer   │   Editor     │   Panel       │
│              │              │               │
│  (Left)      │  (Center)    │  (Right)      │
│              │              │               │
├──────────────┴──────────────┴───────────────┤
│  Build | Logcat | Terminal | Problems      │
│  (Bottom Panels)                            │
└─────────────────────────────────────────────┘
```

### 3. အစိတ်အပိုင်းတစ်ခုချင်းဆီ

#### A. Top App Bar (ထိပ်ခြင်း)
```
┌─────────────────────────────────────────┐
│ 🔷 Android Studio Mini                  │
│ [New] [Save] [Run] [Settings] [Help]   │
└─────────────────────────────────────────┘
```

**ခလုတ်များ:**
- **New**: নစ်ပ project ဖန်တီးခြင်း
- **Save**: လက်ရှိ ဖိုင်ကို သိမ်းဆည်းခြင်း
- **Run**: Project ကို ဖန်တီးခြင်း (APK ဖန်တီးခြင်း)
- **Settings**: အပ်လီကေးရှင်း ဆက်တင်များ
- **Help**: အကူအညီ ရယူခြင်း

#### B. Project Explorer (ဘယ်ခြင်း)
```
Project Structure:
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   │       └── values/
│   │   │           └── strings.xml
│   │   └── test/
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

**အသုံးပြုခြင်း:**
- ဖိုင်ကို နှိပ်ခြင်း - ကုဒ်အယ်ဒီတာတွင် ဖွင့်ခြင်း
- ဖိုင်ပေါ်တွင် အလုံးအရင်း နှိပ်ခြင်း - အခြင်းအရာ menu ပွင့်ခြင်း
- Folder ကို နှိပ်ခြင်း - ကျုံးခြင်း/ချဲ့ခြင်း

#### C. Code Editor (အလယ်ခြင်း)
```
┌──────────────────────────────────────┐
│ MainActivity.kt [x] Utils.kt [x]    │  ← Tabs
├──────────────────────────────────────┤
│ 1  | package com.example.app         │
│ 2  |                                 │
│ 3  | import android.os.Bundle       │
│ 4  |                                 │
│ 5  | class MainActivity : Activity { │
│    |                                 │
│    | [Undo] [Redo] [Format] [Find]  │  ← Toolbar
├──────────────────────────────────────┤
│ Line 5, Column 10 | Kotlin           │  ← Status Bar
└──────────────────────────────────────┘
```

**အသုံးပြုခြင်း:**
- ကုဒ်ကို ရိုက်ခြင်း - ကုဒ်ရေးသားခြင်း
- Tab ကို နှိပ်ခြင်း - ဖိုင်များ ကူးပြောင်းခြင်း
- Undo ခလုတ် - ယခင်အခြင်းအရာသို့ ပြန်သွားခြင်း
- Format ခလုတ် - ကုဒ်ကို အလှတစ်ခြင်းခြင်း

#### D. Inspector Panel (ညာခြင်း)
```
┌──────────────────────────┐
│ File Properties          │
├──────────────────────────┤
│ Name: MainActivity.kt    │
│ Type: Kotlin Source      │
│ Size: 2.5 KB            │
│ Modified: 09:30:45      │
│ Encoding: UTF-8         │
└──────────────────────────┘
```

#### E. Bottom Panels (အောက်ခြင်း)

**Tab 1: Build**
```
Build Output:
[INFO] Compiling resources...
[INFO] Compiling Java/Kotlin...
[INFO] Converting to DEX...
[INFO] Packaging APK...
[SUCCESS] Build completed in 45s
```

**Tab 2: Logcat**
```
System Logs:
[I/System] Application started
[D/MainActivity] onCreate() called
[W/Network] Slow connection detected
[E/Database] Connection failed
```

**Tab 3: Terminal**
```
Command interface for running commands
$ gradle build
$ adb install app.apk
$ ./gradlew test
```

**Tab 4: Problems**
```
Issues found:
[ERROR] Missing file: strings.xml
[WARNING] TODO: Implement feature
[INFO] Unused variable: temp
```

---

## အဓိကအလုပ်ဆောင်ချက်များ

### 4. Project ဖန်တီးခြင်း

#### Step 1: "New" ခလုတ်ကို နှိပ်ခြင်း
```
Top App Bar တွင် [New] ခလုတ်ကို နှိပ်ခြင်း
```

#### Step 2: Template ရွေးချယ်ခြင်း
```
┌─────────────────────────────────────┐
│ Select Project Template             │
├─────────────────────────────────────┤
│ ☐ Empty Project                     │
│ ☐ Basic Activity                    │
│ ☑ Jetpack Compose (Selected)       │
│ ☐ Android Library                   │
│ ☐ Multi-Module Project              │
├─────────────────────────────────────┤
│ [Cancel]  [Create]                  │
└─────────────────────────────────────┘
```

#### Step 3: Project အချက်အလက် ထည့်သွင်းခြင်း
```
┌─────────────────────────────────────┐
│ Create New Project                  │
├─────────────────────────────────────┤
│ Project Name: My First App          │
│ Package Name: com.example.myfirstapp│
│ Location: /storage/emulated/0/...   │
├─────────────────────────────────────┤
│ [Cancel]  [Create]                  │
└─────────────────────────────────────┘
```

#### Step 4: Project ဖန်တီးခြင်း
```
✓ Project structure ဖန်တီးခြင်း
✓ Gradle files ဖန်တီးခြင်း
✓ Source files ဖန်တီးခြင်း
✓ Resource files ဖန်တီးခြင်း
✓ Project အဆင်သင့်
```

---

### 5. ကုဒ်ရေးသားခြင်း

#### Step 1: ဖိုင်ကို ရွေးချယ်ခြင်း
```
Project Explorer တွင် MainActivity.kt ကို နှိပ်ခြင်း
```

#### Step 2: Code Editor တွင် ကုဒ်ရေးသားခြင်း
```kotlin
package com.example.myfirstapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ဒီတွင် ကုဒ်ရေးသားခြင်း
        println("Hello, Android Studio Mini!")
    }
}
```

#### Step 3: ကုဒ်ကို သိမ်းဆည်းခြင်း
```
Top App Bar တွင် [Save] ခလုတ်ကို နှိပ်ခြင်း
(သို့မဟုတ် Ctrl+S)
```

---

### 6. Project ကို ဖန်တီးခြင်း (APK)

#### Step 1: Build ခလုတ်ကို နှိပ်ခြင်း
```
Top App Bar တွင် [Run] ခလုတ်ကို နှိပ်ခြင်း
```

#### Step 2: Build Progress Dialog ပွင့်ခြင်း
```
┌─────────────────────────────────────┐
│ Building Project...                 │
├─────────────────────────────────────┤
│ ████████░░░░░░░░░░░░░░░░░░░░ 35%  │
├─────────────────────────────────────┤
│ Build Logs:                         │
│ [INFO] Validating project...        │
│ [INFO] Compiling resources...       │
│ [INFO] Compiling Java/Kotlin...     │
│ [INFO] Converting to DEX...         │
│ [INFO] Packaging APK...             │
├─────────────────────────────────────┤
│ [Cancel]                            │
└─────────────────────────────────────┘
```

#### Step 3: Build ပြီးဆုံးခြင်း
```
┌─────────────────────────────────────┐
│ ✓ Build Successful                  │
├─────────────────────────────────────┤
│ APK Path: /app/build/outputs/apk/   │
│ Size: 25 MB                         │
│ Duration: 45s 234ms                 │
├─────────────────────────────────────┤
│ [Close]  [Install]                  │
└─────────────────────────────────────┘
```

#### Step 4: APK ကို ထည့်သွင်းခြင်း
```
[Install] ခလုတ်ကို နှိပ်ခြင်း
APK ကို ဖုန်းတွင် အလုံးလုံ ထည့်သွင်းခြင်း
```

---

### 7. ဖိုင်များကို စီမံခန့်ခွဲခြင်း

#### ဖိုင်ဖန်တီးခြင်း
```
Project Explorer တွင် Folder ပေါ်တွင် အလုံးအရင်း နှိပ်ခြင်း
→ "New File" ရွေးချယ်ခြင်း
→ ဖိုင်အမည် ထည့်သွင်းခြင်း
→ "Create" ခလုတ်ကို နှိပ်ခြင်း
```

#### ဖိုင်ပြင်ဆင်ခြင်း
```
ဖိုင်ကို Code Editor တွင် ဖွင့်ခြင်း
ကုဒ်ကို ပြင်ဆင်ခြင်း
[Save] ခလုတ်ကို နှိပ်ခြင်း
```

#### ဖိုင်ဖျက်ခြင်း
```
Project Explorer တွင် ဖိုင်ပေါ်တွင် အလုံးအရင်း နှိပ်ခြင်း
→ "Delete" ရွေးချယ်ခြင်း
→ "Confirm" ခလုတ်ကို နှိပ်ခြင်း
```

---

### 8. Settings ကို ပြင်ဆင်ခြင်း

#### Settings Dialog ကို ဖွင့်ခြင်း
```
Top App Bar တွင် [Settings] ခလုတ်ကို နှိပ်ခြင်း
```

#### ရွေးချယ်နိုင်သည့် Settings များ
```
┌─────────────────────────────────────┐
│ Settings                            │
├─────────────────────────────────────┤
│ ☑ Enable Minification               │
│ ☑ Enable Debugging                  │
│ ☐ Shrink Resources                  │
│ ☑ Multi-Dex Support                │
│ ☑ Incremental Builds                │
│ ☑ Parallel Compilation              │
│ ☑ Build Caching                     │
│ ☑ Code Optimization                 │
├─────────────────────────────────────┤
│ [Reset] [Apply] [Cancel]            │
└─────────────────────────────────────┘
```

---

## အဆင့်မြင့်အသုံးပြုခြင်း

### 9. Dependencies ထည့်သွင်းခြင်း

#### Step 1: Build Options ကို ဖွင့်ခြင်း
```
Settings Dialog တွင် "Dependencies" ကို ရွေးချယ်ခြင်း
```

#### Step 2: Library ရွေးချယ်ခြင်း
```
┌─────────────────────────────────────┐
│ Add Dependencies                    │
├─────────────────────────────────────┤
│ ☐ AndroidX AppCompat               │
│ ☐ Material Design                   │
│ ☑ Jetpack Compose                  │
│ ☐ Retrofit                          │
│ ☐ Room Database                     │
│ ☐ Firebase                          │
│ ☐ Glide Image Loading               │
├─────────────────────────────────────┤
│ [Cancel]  [Add]                     │
└─────────────────────────────────────┘
```

#### Step 3: Dependencies သိမ်းဆည်းခြင်း
```
[Add] ခလုတ်ကို နှိပ်ခြင်း
build.gradle.kts ဖိုင်တွင် အလုံးလုံ ထည့်သွင်းခြင်း
```

---

### 10. Project ကို ခွဲခြမ်းစိတ်ဖြာခြင်း

#### Step 1: Analyze ခလုတ်ကို နှိပ်ခြင်း
```
Settings Dialog တွင် "Analyze Project" ကို ရွေးချယ်ခြင်း
```

#### Step 2: Analysis Report ကြည့်ခြင်း
```
┌─────────────────────────────────────┐
│ Project Analysis Report             │
├─────────────────────────────────────┤
│ Source Files: 12                    │
│ Test Files: 8                       │
│ Total Lines: 2,456                  │
│                                     │
│ Code Quality:                       │
│ Complexity: 45/100                  │
│ Style: 78/100                       │
│ Security: 92/100                    │
│ Maintainability: 85/100             │
│                                     │
│ Issues Found: 3                     │
│ [✓] View Details                    │
├─────────────────────────────────────┤
│ [Close]                             │
└─────────────────────────────────────┘
```

---

### 11. Test များ ဆောင်ရွက်ခြင်း

#### Step 1: Test ခလုတ်ကို နှိပ်ခြင်း
```
Settings Dialog တွင် "Run Tests" ကို ရွေးချယ်ခြင်း
```

#### Step 2: Test Results ကြည့်ခြင်း
```
┌─────────────────────────────────────┐
│ Test Results                        │
├─────────────────────────────────────┤
│ Total: 15  Passed: 14 ✓  Failed: 1 ✗│
│ Duration: 234ms                     │
│                                     │
│ ✓ UtilsTest.testFormatString        │
│ ✓ UtilsTest.testParseJson           │
│ ✗ UtilsTest.testCalculateSum        │
│   Error: Expected 5 but got 6       │
├─────────────────────────────────────┤
│ [Close]                             │
└─────────────────────────────────────┘
```

---

## အဖြေများ (FAQ)

### Q1: APK ဖိုင်ကို ဘယ်သို့ ရယူမည်နည်း?

**A:** Build ပြီးဆုံးပါက "Install" ခလုတ်ကို နှိပ်ခြင်း။ APK ဖိုင်သည် အောက်ခြင်း location တွင် သိမ်းဆည်းထားသည်:
```
/storage/emulated/0/Android/data/com.example.androidstudiomini/files/
```

### Q2: ကုဒ်မှားသွားပါက ဘယ်လို ပြင်ဆင်မည်နည်း?

**A:** 
1. Code Editor တွင် မှားသည့် ကုဒ်ကို ရှာခြင်း
2. ကုဒ်ကို ပြင်ဆင်ခြင်း
3. [Save] ခလုတ်ကို နှိပ်ခြင်း
4. Project ကို ပြန်ဖန်တီးခြင်း

### Q3: Build မြန်မြန်လုပ်ရန် ဘယ်လို လုပ်မည်နည်း?

**A:** Settings တွင် အောက်ပါများကို ဖွင့်ခြင်း:
- Incremental Builds
- Parallel Compilation
- Build Caching

### Q4: APK အရွယ်ကို ကျေးဇူးပြု၍ လျှော့ချပါ။

**A:** Settings တွင် အောက်ပါများကို ဖွင့်ခြင်း:
- Enable Minification
- Shrink Resources
- Code Optimization

### Q5: ဖုန်းတွင် APK ကို အလုံးလုံ ထည့်သွင်းလို့ မရပါက ဘယ်လို လုပ်မည်နည်း?

**A:**
1. Settings → Security → Unknown Sources ကို ဖွင့်ခြင်း
2. Storage space လုံလောက်ကြောင်း စစ်ခြင်း (500MB အနည်းဆုံး)
3. ဖုန်းကို restart လုပ်ခြင်း
4. ပြန်လည် ထည့်သွင်းခြင်း

---

## အကျ အကျိုးအရှိုးများ

### ✅ အကျိုးအရှိုးများ
- PC မလိုအပ်ခြင်း
- ဖုန်းတွင် အလုံးလုံ ကုဒ်ရေးသားနိုင်ခြင်း
- အလျင်အမြန် ဖန်တီးနိုင်ခြင်း
- အခမဲ့ အသုံးပြုနိုင်ခြင်း
- အဖွင့်အစ source code

### ⚠️ ကန့်သတ်ချက်များ
- အလယ်အလတ် အရွယ်ကြီး project များ အတွက် အကောင်းဆုံး
- ကြီးမားသည့် library များ အတွက် ကန့်သတ်ခြင်း
- Debugger အဆင့်မြင့် features မရှိခြင်း
- Network ကြည်လင်မှု အလျင်အမြန် လိုအပ်ခြင်း

---

## အကူအညီ ရယူခြင်း

### ပြဿနာများ ကြုံတွေ့ပါက:

1. **Help ခလုတ်ကို နှိပ်ခြင်း** - အပ်လီကေးရှင်း အတွင်း အကူအညီ
2. **Documentation ကြည့်ခြင်း** - README.md, USER_GUIDE.md
3. **ဖုန်းကို Restart လုပ်ခြင်း** - အများအပြားပြဿနာ ဖြေရှင်းခြင်း
4. **APK ကို ပြန်ထည့်သွင်းခြင်း** - အပ်လီကေးရှင်း ပြန်စတင်ခြင်း

---

## အကျဉ်းချုပ်

Android Studio Mini IDE သည် **ဖုန်းတွင် အလုံးလုံ Android ကုဒ်ရေးသားနိုင်သည့်** အစွမ်းအစကြီးမားသည့် IDE ဖြစ်သည်။ 

**အဓိကအသုံးပြုခြင်း:**
1. ✅ Project ဖန်တီးခြင်း
2. ✅ ကုဒ်ရေးသားခြင်း
3. ✅ APK ဖန်တီးခြင်း
4. ✅ ဖုန်းတွင် အလုံးလုံ ထည့်သွင်းခြင်း
5. ✅ Test ဆောင်ရွက်ခြင်း

**အခုလက်ခြင်း Android Studio Mini IDE ကို အသုံးပြုပြီး ကုဒ်ရေးသားခြင်းကို စတင်ပါ!** 🚀

---

**Version**: 1.0  
**Last Updated**: June 24, 2026  
**Language**: Myanmar (Burmese)
