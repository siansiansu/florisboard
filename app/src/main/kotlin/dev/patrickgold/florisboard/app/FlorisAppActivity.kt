/*
 * Copyright (C) 2021-2025 The FlorisBoard Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.patrickgold.florisboard.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dev.patrickgold.florisboard.R
import dev.patrickgold.florisboard.app.apptheme.FlorisAppTheme
import dev.patrickgold.florisboard.appContext
import dev.patrickgold.florisboard.lib.FlorisLocale
import org.florisboard.lib.android.AndroidVersion
import org.florisboard.lib.android.hideAppIcon
import org.florisboard.lib.android.showAppIcon
import org.florisboard.lib.compose.ProvideLocalizedResources
import org.florisboard.lib.compose.conditional
import org.florisboard.lib.kotlin.collectIn
import java.util.concurrent.atomic.AtomicBoolean

enum class AppTheme(val id: String) {
    AUTO("auto"),
    AUTO_AMOLED("auto_amoled"),
    LIGHT("light"),
    DARK("dark"),
    AMOLED_DARK("amoled_dark");
}

class FlorisAppActivity : ComponentActivity() {
    private val prefs by FlorisPreferenceStore
    private val appContext by appContext()
    private var appTheme by mutableStateOf(AppTheme.AUTO)
    private var showAppIcon = true
    private var resourcesContext by mutableStateOf(this as Context)

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash screen should be installed before calling super.onCreate()
        installSplashScreen().apply {
            setKeepOnScreenCondition { !appContext.preferenceStoreLoaded.value }
        }
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        prefs.other.settingsTheme.asFlow().collectIn(lifecycleScope) {
            appTheme = it
        }
        prefs.other.settingsLanguage.asFlow().collectIn(lifecycleScope) {
            val config = Configuration(resources.configuration)
            val locale = if (it == "auto") FlorisLocale.default() else FlorisLocale.fromTag(it)
            config.setLocale(locale.base)
            resourcesContext = createConfigurationContext(config)
        }
        if (AndroidVersion.ATMOST_API28_P) {
            prefs.other.showAppIcon.asFlow().collectIn(lifecycleScope) {
                showAppIcon = it
            }
        }

        // We defer the setContent call until the datastore model is loaded, until then the splash screen stays drawn
        val isModelLoaded = AtomicBoolean(false)
        appContext.preferenceStoreLoaded.collectIn(lifecycleScope) { loaded ->
            if (!loaded || isModelLoaded.getAndSet(true)) return@collectIn
            setContent {
                ProvideLocalizedResources(
                    resourcesContext,
                    appName = R.string.app_name,
                ) {
                    FlorisAppTheme(theme = appTheme) {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            AppContent()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // App icon visibility control was restricted in Android 10.
        // See https://developer.android.com/reference/android/content/pm/LauncherApps#getActivityList(java.lang.String,%20android.os.UserHandle)
        if (AndroidVersion.ATMOST_API28_P) {
            if (showAppIcon) {
                this.showAppIcon()
            } else {
                this.hideAppIcon()
            }
        }
    }

    @Composable
    private fun AppContent() {
        // Empty app content - settings UI removed
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .conditional(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    displayCutoutPadding()
                }
                .imePadding(),
        ) {
            // Blank page
        }
    }
}
