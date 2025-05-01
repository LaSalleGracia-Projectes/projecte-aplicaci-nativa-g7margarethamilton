package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import android.content.Context
import java.util.Locale

fun Context.setLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    return createConfigurationContext(config)
}
