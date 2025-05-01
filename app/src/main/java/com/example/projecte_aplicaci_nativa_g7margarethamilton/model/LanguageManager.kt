package com.example.projecte_aplicaci_nativa_g7margarethamilton.model

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageManager {

    fun apply(langCode: String) {
        val localeList = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(localeList)   // ✨
    }

    fun clear() = AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
}
