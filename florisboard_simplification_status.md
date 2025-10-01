# FlorisBoard 簡化專案 - 目前狀況報告

## 執行日期
2025-10-01

## 總體進度
- **狀態**: 🟡 進行中（大部分完成，剩餘少量編譯錯誤）
- **檔案數量**: 約 100+ 個 Kotlin 檔案（從原本 180 個減少）
- **編譯狀態**: 剩餘極少數編譯錯誤需要修復

## 已移除的模組

### ✅ 完全移除的功能

#### 1. UI 與設定模組
- ✅ `app/setup/` - Onboarding 設定流程
- ✅ `app/settings/` - 所有設定頁面 UI（保留 theme/ 基礎類別）
- ✅ `app/devtools/` - 開發者工具 UI
- ✅ `app/ext/` - 擴充功能管理 UI

#### 2. 輸入法進階功能
- ✅ `ime/nlp/` - 自然語言處理模組（建議詞、拼寫檢查）
- ✅ `ime/clipboard/` - 剪貼簿管理
- ✅ `ime/dictionary/` - 字典管理
- ✅ `ime/smartbar/` - 智慧列（建議列）
- ✅ `ime/text/composing/` - 日韓文輸入法
- ✅ `ime/onehanded/` - 單手模式
- ✅ `ime/landscapeinput/` - 橫向輸入特殊模式
- ✅ `ime/sheet/` - Bottom sheet 面板
- ✅ `ime/media/emoticon/` - Emoticon 功能

#### 3. 擴充功能系統
- ✅ `lib/ext/` - 擴充功能框架
- ✅ FlorisSpellCheckerService - 拼寫檢查服務

## 已修復的核心檔案

### ✅ 完全修復
1. **FlorisApplication.kt**
   - 移除 ExtensionManager、NlpManager、DictionaryManager
   - 簡化初始化流程

2. **FlorisImeService.kt**
   - 移除 NlpInlineAutofill、DevtoolsOverlay、OneHandedPanel
   - 簡化 BottomSheetHostUi（只保留 subtype 選擇）
   - 移除 Smartbar 相關邏輯
   - 添加 clickable import

3. **AppPrefs.kt**
   - 移除 devtools、dictionary、glide 內部類別
   - 移除 smartbar、spelling、suggestion 偏好設定
   - 簡化 fontSizeMultiplier()（移除 OneHandedMode 邏輯）
   - 簡化主題設定（移除 ExtensionComponentName）

4. **EnumDisplayEntries.kt**
   - 移除 CandidatesDisplayMode、ExtendedActionsPlacement
   - 移除 IncognitoDisplayMode、LandscapeInputUiMode
   - 移除 OneHandedMode、SmartbarLayout、SpellingLanguageMode

5. **Routes.kt**
   - 移除所有設定/開發工具/擴充功能畫面的引用
   - 簡化 AppNavHost 為空函數
   - 添加 dummy enum types（LanguagePackManagerScreenAction 等）

6. **FlorisAppActivity.kt**
   - 顯示空白畫面（所有導航已移除）

## 保留的核心功能

### ✅ 基本鍵盤功能
- ✅ **英文鍵盤佈局** - 完整保留 QWERTY 佈局
- ✅ **鍵盤類型切換** - CHARACTERS, SYMBOLS, NUMERIC, PHONE 等
- ✅ **按鍵輸入處理** - 基本的按鍵事件與文字輸入
- ✅ **長按彈出鍵** - popup/ 模組完整保留
- ✅ **手勢支援** - text/gestures/ 保留（滑動切換、刪除等）
- ✅ **觸控回饋** - 震動、音效回饋

### ✅ 媒體輸入
- ✅ **Emoji 輸入** - ime/media/emoji/ 完整保留
- ✅ **Emoji 分類** - 表情符號分類瀏覽
- ✅ **Emoji 歷史** - 最近使用記錄
- ✅ **Emoji 搜尋** - 基本搜尋功能

### ✅ 主題系統
- ✅ **Dark/Light Mode** - 跟隨系統或手動切換
- ✅ **主題管理** - ThemeManager 完整保留
- ✅ **主題樣式** - Snygg 樣式系統保留
- ✅ **顏色配置** - 基本顏色設定

### ✅ 文字編輯
- ✅ **EditorInstance** - 編輯器核心邏輯
- ✅ **文字選取** - 選取、複製、貼上
- ✅ **游標控制** - 游標移動與定位
- ✅ **自動大小寫** - 基本的大小寫行為

### ✅ 多語言支援
- ✅ **Subtype 系統** - 語言切換（雖然目前設定為單一語言）
- ✅ **語言選擇面板** - SelectSubtypePanel 保留

## 剩餘的編譯錯誤

### 🔴 需要修復的檔案

#### 1. ThemeExtension.kt (line 56)
```
'build' overrides nothing
```
**影響**: 主題擴充系統
**需要**: 檢查並修復 build() 方法

#### 2. Subtype.kt
**預期問題**: ExtensionComponentName 相關引用
**影響**: 語言/鍵盤佈局定義
**需要**: 移除或替換 ExtensionComponentName

#### 3. AbstractEditorInstance.kt
**預期問題**: BreakIteratorGroup、Composer 等 NLP 相關引用
**影響**: 文字編輯核心
**需要**: 移除 NLP 相關邏輯，保留基本編輯功能

## 檔案結構對比

### 原始結構（180 檔案）
```
app/
├── setup/          ❌ 已移除
├── settings/       ❌ 已移除（除了 theme/ 基礎）
├── devtools/       ❌ 已移除
├── ext/            ❌ 已移除
└── apptheme/       ✅ 保留

ime/
├── core/           ✅ 保留
├── editor/         ✅ 保留
├── input/          ✅ 保留
├── keyboard/       ✅ 保留
├── media/emoji/    ✅ 保留
├── popup/          ✅ 保留
├── text/           ✅ 保留（gestures, key, keyboard）
├── theme/          ✅ 保留
├── clipboard/      ❌ 已移除
├── dictionary/     ❌ 已移除
├── nlp/            ❌ 已移除
├── smartbar/       ❌ 已移除
├── onehanded/      ❌ 已移除
├── landscapeinput/ ❌ 已移除
└── sheet/          ❌ 已移除

lib/
├── cache/          ✅ 保留
├── compose/        ✅ 保留
├── crashutility/   ✅ 保留
├── devtools/       ✅ 保留（基礎框架）
├── io/             ✅ 保留
├── util/           ✅ 保留
└── ext/            ❌ 已移除
```

### 目前結構（約 100+ 檔案）
```
app/
├── apptheme/       ✅ 4 檔案
├── settings/theme/ ✅ 2 檔案（基礎類別）
├── AppPrefs.kt     ✅ 已簡化
├── Routes.kt       ✅ 已簡化
├── EnumDisplayEntries.kt ✅ 已簡化
└── FlorisAppActivity.kt  ✅ 空白畫面

ime/
├── core/           ✅ 4 檔案
├── editor/         ✅ 7 檔案
├── input/          ✅ 6 檔案
├── keyboard/       ✅ 17 檔案
├── media/emoji/    ✅ 10 檔案
├── popup/          ✅ 4 檔案
├── text/           ✅ 20 檔案
└── theme/          ✅ 12 檔案

lib/
├── cache/          ✅ 1 檔案
├── compose/        ✅ 7 檔案
├── crashutility/   ✅ 2 檔案
├── devtools/       ✅ 3 檔案
└── util/           ✅ 多個工具檔案
```

## 下一步工作

### 🔴 緊急修復（編譯錯誤）
1. 修復 ThemeExtension.kt:56 的 build() 方法
2. 檢查並修復 Subtype.kt 的 ExtensionComponentName 引用
3. 檢查並修復 AbstractEditorInstance.kt 的 NLP 引用

### 🟡 功能驗證
1. 測試基本鍵盤輸入
2. 測試鍵盤類型切換（CHARACTERS ↔ SYMBOLS ↔ NUMERIC）
3. 測試 Emoji 輸入
4. 測試 Dark/Light mode 切換

### 🟢 優化項目（可選）
1. 移除 Subtype 系統（如果確定只需要單一語言）
2. 進一步簡化主題系統
3. 清理未使用的資源檔案
4. 移除 lib/devtools 基礎框架

## 成果統計

| 項目 | 原始 | 目前 | 減少 |
|------|------|------|------|
| Kotlin 檔案 | ~180 | ~100+ | ~40-45% |
| app/ 模組 | 30 | 11 | 63% |
| ime/ 模組 | 109 | ~80 | 27% |
| 已移除模組 | - | 9 | - |
| 編譯錯誤 | 100+ | <5 | >95% |

## 保留功能確認

✅ **基本英文鍵盤** - QWERTY 佈局完整
✅ **鍵盤類型切換** - CHARACTERS, SYMBOLS, NUMERIC, PHONE
✅ **Emoji 輸入** - 完整的 Emoji 選擇器
✅ **Dark Mode** - 主題系統保留
✅ **觸控回饋** - 震動、音效
✅ **長按彈出鍵** - 特殊符號輸入
✅ **手勢操作** - 滑動切換、刪除

## 備註

- 專案已大幅簡化，移除約 40-45% 的程式碼
- 核心 IME 功能完整保留
- 目前只剩下少數編譯錯誤需要修復
- 基本鍵盤功能（包括類型切換）均已保留
- 主題系統和 Emoji 輸入完整保留
