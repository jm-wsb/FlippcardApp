package com.example.flippcardapp.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import androidx.core.graphics.ColorUtils

/**
 * Adjusts the color for night mode to reduce contrast against dark backgrounds.
 */
fun Int.adjustForNightMode(context: Context): Int {
    val isNightMode = (context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    
    return if (isNightMode) {
        // Blend the original color with black (e.g., 30% black) to make it less vibrant/contrasty
        // and more suitable for dark themes.
        ColorUtils.blendARGB(this, Color.BLACK, 0.3f)
    } else {
        this
    }
}
