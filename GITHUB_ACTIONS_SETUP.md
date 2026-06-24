# GitHub Actions Setup Guide - Android Studio Mini

## အကျဉ်းချုပ်

ဤ guide သည် Android Studio Mini project အတွက် GitHub Actions ကို အသုံးပြု၍ APK ဖိုင်ကို အလုံးလုံ build လုပ်ခြင်းကို ရှင်းပြထားသည်။

---

## 1. Workflow Files

### 📁 Workflow Location

```
.github/workflows/
├── build-apk.yml              # Gradle-based build
├── build-apk-custom.yml       # Custom build tools
└── test-and-lint.yml          # Testing & quality checks
```

### 📋 Workflow Files အကြောင်း

#### 1.1 build-apk.yml (Gradle-based)

**အခန်းကဏ္ဍ:**
- Standard Gradle build system အသုံးပြုခြင်း
- Android SDK အလုံးလုံ setup လုပ်ခြင်း
- Debug & Release APK ဖန်တီးခြင်း
- Artifacts upload လုပ်ခြင်း

**Triggers:**
- Push to main/develop branches
- Pull requests to main
- Manual trigger (workflow_dispatch)

**Steps:**
1. Code checkout
2. JDK 11 setup
3. Android SDK setup
4. Gradle build
5. APK assembly (Debug & Release)
6. Artifact upload

#### 1.2 build-apk-custom.yml (Custom Build Tools)

**အခန်းကဏ္ဍ:**
- aapt2, d8, apksigner အသုံးပြုခြင်း
- Resource compilation (aapt2)
- Source compilation (javac/kotlinc)
- DEX conversion (d8)
- APK signing (apksigner)
- APK alignment (zipalign)

**Triggers:**
- Push to main/develop branches
- Pull requests to main
- Manual trigger

**Steps:**
1. Code checkout
2. JDK 11 setup
3. Kotlin setup
4. Download build tools
5. Compile resources
6. Compile sources
7. Convert to DEX
8. Package APK
9. Sign APK
10. Align APK
11. Verify APK
12. Upload artifacts

#### 1.3 test-and-lint.yml

**အခန်းကဏ္ဍ:**
- Unit tests ဆောင်ရွက်ခြင်း
- Lint checks လုပ်ခြင်း
- Test reports upload လုပ်ခြင်း

**Triggers:**
- Push to main/develop
- Pull requests to main

**Steps:**
1. Code checkout
2. JDK 11 setup
3. Run unit tests
4. Run lint checks
5. Upload test results

---

## 2. Setup Instructions

### Step 1: Repository Setup

```bash
# Clone repository
git clone https://github.com/your-username/AndroidStudioMini.git
cd AndroidStudioMini

# Create GitHub workflows directory
mkdir -p .github/workflows

# Copy workflow files
cp build-apk.yml .github/workflows/
cp build-apk-custom.yml .github/workflows/
cp test-and-lint.yml .github/workflows/

# Commit and push
git add .github/
git commit -m "Add GitHub Actions workflows"
git push origin main
```

### Step 2: GitHub Secrets Setup

GitHub repository settings မှ secrets ထည့်သွင်းခြင်း:

```
Settings → Secrets and variables → Actions → New repository secret
```

**Required Secrets:**

1. **KEYSTORE_BASE64** (Optional - for Release builds)
   ```bash
   # Generate keystore
   keytool -genkey -v -keystore debug.keystore \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias debug -storepass android -keypass android \
     -dname "CN=Android Studio Mini, O=Development, C=MM"
   
   # Convert to base64
   base64 -w 0 debug.keystore > keystore.txt
   
   # Copy content to GitHub secret
   ```

2. **GITHUB_TOKEN** (Automatic - no setup needed)

### Step 3: Verify Workflow Files

```bash
# Check workflow syntax
git check-attr -a .github/workflows/build-apk.yml

# Validate YAML
yamllint .github/workflows/*.yml
```

---

## 3. Workflow Usage

### 3.1 Automatic Triggers

**Push to main/develop:**
```bash
git push origin main
# Workflow automatically triggers
```

**Pull Request:**
```bash
# Create PR to main
# Workflow automatically runs
```

### 3.2 Manual Trigger

GitHub UI မှ manual trigger လုပ်ခြင်း:

```
Actions → Select Workflow → Run workflow
```

Or via CLI:
```bash
gh workflow run build-apk.yml --ref main
```

### 3.3 Monitor Workflow

**GitHub UI:**
```
Actions → Select workflow run → View logs
```

**CLI:**
```bash
gh run list --workflow=build-apk.yml
gh run view <RUN_ID> --log
```

---

## 4. Workflow Customization

### 4.1 Change Build Configuration

**build-apk.yml တွင် modify လုပ်ခြင်း:**

```yaml
# Android SDK version ပြောင်းလဲခြင်း
- name: Install Android SDK components
  run: |
    sdkmanager "platforms;android-35"  # Change version
    sdkmanager "build-tools;35.0.0"    # Change version
```

### 4.2 Add Custom Build Steps

```yaml
- name: Custom build step
  run: |
    echo "Custom command here"
    ./custom-script.sh
```

### 4.3 Change Artifact Retention

```yaml
- name: Upload APK artifacts
  uses: actions/upload-artifact@v3
  with:
    name: APK-Artifacts
    path: app/build/outputs/apk/**/*.apk
    retention-days: 60  # Change retention days
```

### 4.4 Add Notifications

**Slack notification:**
```yaml
- name: Notify Slack
  uses: slackapi/slack-github-action@v1
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload: |
      {
        "text": "APK build completed!"
      }
```

---

## 5. Build Outputs

### 5.1 Artifact Locations

**GitHub Actions UI:**
```
Actions → Workflow run → Artifacts
```

**Download artifacts:**
```bash
gh run download <RUN_ID> -n APK-Artifacts
```

### 5.2 APK Output Paths

**Debug APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

**Release APK:**
```
app/build/outputs/apk/release/app-release.apk
```

**Custom build APK:**
```
app/build/outputs/apk/AndroidStudioMini-debug.apk
```

---

## 6. Troubleshooting

### Issue 1: Build fails with "SDK not found"

**Solution:**
```yaml
- name: Set up Android SDK
  uses: android-actions/setup-android@v2
  with:
    api-level: 34
    build-tools-version: 34.0.0
```

### Issue 2: Gradle build timeout

**Solution:**
```yaml
- name: Build with Gradle
  run: ./gradlew build
  timeout-minutes: 60
```

### Issue 3: Keystore not found

**Solution:**
```yaml
- name: Create debug keystore
  run: |
    mkdir -p ~/.android
    keytool -genkey -v -keystore ~/.android/debug.keystore \
      -keyalg RSA -keysize 2048 -validity 10000 \
      -alias debug -storepass android -keypass android \
      -dname "CN=Android Studio Mini, O=Development, C=MM"
```

### Issue 4: APK signing fails

**Solution:**
```yaml
- name: Verify keystore
  run: |
    keytool -list -v -keystore ~/.android/debug.keystore \
      -storepass android
```

---

## 7. Best Practices

### 7.1 Security

✅ **Do:**
- Use GitHub secrets for sensitive data
- Never commit keystores to repository
- Use separate secrets for release builds

❌ **Don't:**
- Hardcode passwords in workflows
- Commit private keys
- Use same keystore for all projects

### 7.2 Performance

✅ **Do:**
- Use caching for dependencies
- Parallelize jobs
- Set appropriate timeouts

❌ **Don't:**
- Download large files repeatedly
- Run unnecessary tests
- Use outdated actions

### 7.3 Maintenance

✅ **Do:**
- Keep workflows updated
- Monitor build times
- Review logs regularly

❌ **Don't:**
- Ignore workflow failures
- Leave old workflows
- Forget to update dependencies

---

## 8. Advanced Configuration

### 8.1 Matrix Builds

```yaml
strategy:
  matrix:
    api-level: [26, 30, 34]
    
steps:
  - name: Build for API ${{ matrix.api-level }}
    run: ./gradlew build -Pandroid.api=${{ matrix.api-level }}
```

### 8.2 Conditional Steps

```yaml
- name: Upload Release APK
  if: startsWith(github.ref, 'refs/tags/')
  uses: actions/upload-artifact@v3
  with:
    name: Release-APK
    path: app/build/outputs/apk/release/*.apk
```

### 8.3 Environment Variables

```yaml
env:
  GRADLE_OPTS: -Xmx2048m
  JAVA_TOOL_OPTIONS: -XX:+UseG1GC

steps:
  - name: Build with env vars
    run: ./gradlew build
```

---

## 9. Monitoring & Analytics

### 9.1 View Workflow Runs

```bash
gh run list --workflow=build-apk.yml --limit 10
```

### 9.2 Check Workflow Status

```bash
gh run view <RUN_ID>
```

### 9.3 Download Logs

```bash
gh run view <RUN_ID> --log > build-log.txt
```

---

## 10. CI/CD Pipeline

### Complete Pipeline

```
Code Push
    ↓
Checkout Code
    ↓
Setup Environment
    ↓
Run Tests
    ↓
Run Lint
    ↓
Build APK
    ↓
Sign APK
    ↓
Upload Artifacts
    ↓
Create Release (if tag)
    ↓
Notify Status
```

---

## အကျဉ်းချုပ်

### Workflow Files

| File | အခန်းကဏ္ဍ | Trigger |
|------|---------|---------|
| build-apk.yml | Gradle build | Push/PR |
| build-apk-custom.yml | Custom tools | Push/PR |
| test-and-lint.yml | Testing | Push/PR |

### Key Features

✅ Automatic APK building  
✅ Custom build tools support  
✅ Testing & quality checks  
✅ Artifact management  
✅ Release automation  
✅ Error notifications  

### Next Steps

1. Copy workflow files to `.github/workflows/`
2. Setup GitHub secrets
3. Push to repository
4. Monitor Actions tab
5. Download APK artifacts

---

**Version**: 1.0  
**Last Updated**: June 24, 2026  
**Language**: Myanmar (Burmese)
