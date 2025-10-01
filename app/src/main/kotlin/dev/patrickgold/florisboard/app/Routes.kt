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

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
// All settings/devtools/extension/setup screens removed - UI removed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

// Dummy types for removed modules (needed for route serialization)
@Serializable
enum class LanguagePackManagerScreenAction { IMPORT, EXPORT }
@Serializable
enum class ThemeManagerScreenAction { IMPORT, EXPORT, EDIT }
@Serializable
enum class UserDictionaryType { SYSTEM, FLORIS }
@Serializable
enum class ExtensionListScreenType { THEME, KEYBOARD, LANGUAGE }
@Serializable
enum class ExtensionImportScreenType { FILE, URL }

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Deeplink(val path: String)

inline fun <reified T : Any> NavGraphBuilder.composableWithDeepLink(
    kClass: KClass<T>,
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit),
) {
    val deeplink = requireNotNull(kClass.annotations.firstOrNull { it is Deeplink } as? Deeplink) {
        "faulty class: $kClass with annotations ${kClass.annotations}"
    }
    composable<T>(
        deepLinks = listOf(navDeepLink<T>(basePath = "ui://florisboard/${deeplink.path}")),
        content = content,
    )
}

object Routes {
    object Setup {
        @Serializable
        object Screen
    }

    object Settings {
        @Serializable
        @Deeplink("settings/home")
        object Home

        @Serializable
        @Deeplink("settings/localization")
        object Localization

        @Serializable
        @Deeplink("settings/localization/select-locale")
        object SelectLocale

        @Serializable
        @Deeplink("settings/localization/language-pack-manage")
        data class LanguagePackManager(val action: LanguagePackManagerScreenAction)

        @Serializable
        @Deeplink("settings/localization/subtype/add")
        object SubtypeAdd

        @Serializable
        @Deeplink("settings/localization/subtype/edit")
        data class SubtypeEdit(val id: Long)

        @Serializable
        @Deeplink("settings/theme")
        object Theme

        @Serializable
        @Deeplink("settings/theme/manage")
        data class ThemeManager(val action: ThemeManagerScreenAction)

        @Serializable
        @Deeplink("settings/keyboard")
        object Keyboard

        @Serializable
        @Deeplink("settings/keyboard/input-feedback")
        object InputFeedback

        @Serializable
        @Deeplink("settings/smartbar")
        object Smartbar

        @Serializable
        @Deeplink("settings/typing")
        object Typing

        @Serializable
        @Deeplink("settings/dictionary")
        object Dictionary

        @Serializable
        @Deeplink("settings/dictionary/user-dictionary")
        data class UserDictionary(val type: UserDictionaryType)

        @Serializable
        @Deeplink("settings/gestures")
        object Gestures

        @Serializable
        @Deeplink("settings/clipboard")
        object Clipboard

        @Serializable
        @Deeplink("settings/media")
        object Media

        @Serializable
        @Deeplink("settings/other")
        object Other

        @Serializable
        @Deeplink("settings/other/physical-keyboard")
        object PhysicalKeyboard

        @Serializable
        @Deeplink("settings/other/backup")
        object Backup

        @Serializable
        @Deeplink("settings/other/restore")
        object Restore

        @Serializable
        @Deeplink("settings/about")
        object About

        @Serializable
        @Deeplink("settings/about/project-license")
        object ProjectLicense

        @Serializable
        @Deeplink("settings/about/third-party-licenses")
        object ThirdPartyLicenses
    }

    object Devtools {
        @Serializable
        @Deeplink("devtools")
        object Home

        @Serializable
        @Deeplink("devtools/android/locales")
        object AndroidLocales

        @Serializable
        @Deeplink("devtools/android/settings")
        data class AndroidSettings(val name: String)

        @Serializable
        @Deeplink("export-debug-log")
        object ExportDebugLog
    }

    object Ext {
        @Serializable
        @Deeplink("ext")
        object Home

        @Serializable
        @Deeplink("ext/list")
        data class List(val type: ExtensionListScreenType, val showUpdate: Boolean? = null)

        @Serializable
        @Deeplink("ext/edit")
        data class Edit(val id: String, @SerialName("create") val serialType: String? = null)

        @Serializable
        @Deeplink("ext/export")
        data class Export(val id: String)

        @Serializable
        @Deeplink("ext/import")
        data class Import(val type: ExtensionImportScreenType, val uuid: String? = null)

        @Serializable
        @Deeplink("ext/view")
        data class View(val id: String)

        @Serializable
        @Deeplink("ext/check-updates")
        object CheckUpdates
    }

    @Composable
    fun AppNavHost(
        modifier: Modifier,
        navController: NavHostController,
        startDestination: KClass<*>,
    ) {
        // All screens removed - navigation disabled
        // App now shows empty screen from FlorisAppActivity
    }
}
