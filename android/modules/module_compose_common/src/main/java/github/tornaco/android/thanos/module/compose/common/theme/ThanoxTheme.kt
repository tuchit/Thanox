/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.android.thanos.module.compose.common.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import com.google.android.material.composethemeadapter3.Mdc3Theme


@Composable
fun ThanoxTheme(
    theme: Theme = Theme(),
    content: @Composable () -> Unit
) {
    val layoutDirection = when (theme.textDirection) {
        TextDirection.LTR -> LayoutDirection.Ltr
        TextDirection.RTL -> LayoutDirection.Rtl
        TextDirection.System -> LocalLayoutDirection.current
    }

    val view = LocalView.current
    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()
    SideEffect {
        WindowCompat.getInsetsController(
            context.findActivity().window,
            view
        )?.isAppearanceLightStatusBars = !darkTheme
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection,
        LocalDensity provides
                Density(
                    density = LocalDensity.current.density,
                    fontScale = theme.fontScale,
                )
    ) {
        // TODO: Remove M2 MaterialTheme when using only M3 components
        androidx.compose.material.MaterialTheme(
            colors = if (darkTheme) darkColors() else lightColors()
        ) {
            Mdc3Theme(
                content = content,
            )
        }
    }
}

@Composable
fun colorSchemeFromThemeMode(
    themeMode: ThemeMode,
    lightColorScheme: ColorScheme,
    darkColorScheme: ColorScheme
): ColorScheme {
    return when (themeMode) {
        ThemeMode.Light -> lightColorScheme
        ThemeMode.Dark -> darkColorScheme
        ThemeMode.System -> if (!isSystemInDarkTheme()) {
            lightColorScheme
        } else {
            darkColorScheme
        }
    }
}

private val LightCustomColorScheme = lightColorScheme(
    primary = Color(0xFF984816),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDBC9),
    onPrimaryContainer = Color(0xFF341000),
    inversePrimary = Color(0xFFFFB68F),
    secondary = Color(0xFF765849),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDBC9),
    onSecondaryContainer = Color(0xFF2B160B),
    tertiary = Color(0xFF656032),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEBE4AA),
    onTertiaryContainer = Color(0xFF1F1C00),
    background = Color(0xFFFCFCFC),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFCFCFC),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF4DED5),
    onSurfaceVariant = Color(0xFF53443D),
    inverseSurface = Color(0xFF362F2C),
    inverseOnSurface = Color(0xFFFBEEE9),
    error = Color(0xFFBA1B1B),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410001),
    outline = Color(0xFF85736B),
)

private val DarkCustomColorScheme = darkColorScheme(
    primary = Color(0xFFFFB68F),
    onPrimary = Color(0xFF562000),
    primaryContainer = Color(0xFF793100),
    onPrimaryContainer = Color(0xFFFFDBC9),
    inversePrimary = Color(0xFF984816),
    secondary = Color(0xFFE6BEAC),
    onSecondary = Color(0xFF432B1E),
    secondaryContainer = Color(0xFF5C4032),
    onSecondaryContainer = Color(0xFFFFDBC9),
    tertiary = Color(0xFFCFC890),
    onTertiary = Color(0xFF353107),
    tertiaryContainer = Color(0xFF4C481C),
    onTertiaryContainer = Color(0xFFEBE4AA),
    background = Color(0xFF201A17),
    onBackground = Color(0xFFEDE0DB),
    surface = Color(0xFF201A17),
    onSurface = Color(0xFFEDE0DB),
    surfaceVariant = Color(0xFF53443D),
    onSurfaceVariant = Color(0xFFD7C2B9),
    inverseSurface = Color(0xFFEDE0DB),
    inverseOnSurface = Color(0xFF362F2C),
    error = Color(0xFFFFB4A9),
    onError = Color(0xFF680003),
    errorContainer = Color(0xFF930006),
    onErrorContainer = Color(0xFFFFDAD4),
    outline = Color(0xFFA08D85),
)

private tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> throw IllegalArgumentException("Could not find activity!")
    }


data class Theme(
    val themeMode: ThemeMode = ThemeMode.System,
    val colorMode: ColorMode = ColorMode.Baseline,
    val fontScale: Float = 1.0f,
    val textDirection: TextDirection = TextDirection.System,
)

/**
 * A class for defining layout directions.
 *
 * A layout direction can be left-to-right (LTR) or right-to-left (RTL).
 */
enum class TextDirection {
    System,

    /** Horizontal layout direction is from Left to Right. */
    LTR,

    /** Horizontal layout direction is from Right to Left. */
    RTL,
}

/**
 * Determines what color scheme should be used when viewing the catalog in the Google Material 3
 * theme.
 */
enum class ColorMode(val label: String) {
    /**
     * The baseline light/dark colors schemes.
     *
     * This is the default behavior, and the fallback if dynamic colors are not available on the
     * current device.
     */
    Baseline("Baseline"),

    /**
     * Build a color scheme from a pre-selected color palette that behaves the same as a dynamic color
     * palette.
     *
     * Useful for testing dynamic color schemes on devices that don't support dynamic colors.
     */
    Custom("Custom"),

    /**
     * Build a color scheme from the dynamic colors taken from the Android System.
     *
     * If the dynamic colors are not available, the baseline color scheme will be used as a fallback.
     */
    Dynamic("Dynamic (Android 12+)"),
}

enum class ThemeMode {
    System,
    Light,
    Dark,
}

val ThemeSaver =
    Saver<Theme, Map<String, Int>>(
        save = { theme ->
            mapOf(
                ThemeModeKey to theme.themeMode.ordinal,
                ColorModeKey to theme.colorMode.ordinal,
                FontScaleKey to theme.fontScale.toInt(),
                TextDirectionKey to theme.textDirection.ordinal,
            )
        },
        restore = { map ->
            Theme(
                themeMode = ThemeMode.values()[map.getValue(ThemeModeKey)],
                colorMode = ColorMode.values()[map.getValue(ColorModeKey)],
                fontScale = map.getValue(FontScaleKey).toFloat(),
                textDirection = TextDirection.values()[map.getValue(TextDirectionKey)],
            )
        }
    )

const val MinFontScale = 0.4f
const val MaxFontScale = 2f

private const val ThemeModeKey = "themeMode"
private const val ColorModeKey = "colorMode"
private const val FontScaleKey = "fontScale"
private const val TextDirectionKey = "textDirection"


