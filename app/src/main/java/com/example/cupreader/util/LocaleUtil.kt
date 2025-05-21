package com.example.cupreader.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleUtil {
    @SuppressLint("ObsoleteSdkInt")
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }
}
