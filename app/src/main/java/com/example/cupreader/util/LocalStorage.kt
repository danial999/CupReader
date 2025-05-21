package com.example.cupreader.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATA_STORE_NAME = "user_prefs"

val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

object LocalStorage {
    private val USER_NAME_KEY      = stringPreferencesKey("user_name")
    private val LANGUAGE_KEY      = stringPreferencesKey("preferred_language")

    suspend fun saveUserName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME_KEY] = name
        }
    }

    fun getUserName(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[USER_NAME_KEY] }

    // Added language methods:
    suspend fun saveLanguage(context: Context, language: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }

    fun getLanguage(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[LANGUAGE_KEY] }

    private val DOB_KEY = stringPreferencesKey("date_of_birth")

    suspend fun saveDob(context: Context, dob: String) {
        context.dataStore.edit { prefs ->
            prefs[DOB_KEY] = dob
        }
    }

    fun getDob(context: Context): Flow<String?> =
        context.dataStore
            .data
            .map { prefs -> prefs[DOB_KEY] }

    private val GENDER_KEY = stringPreferencesKey("gender")

    suspend fun saveGender(context: Context, gender: String) {
        context.dataStore.edit { prefs ->
            prefs[GENDER_KEY] = gender
        }
    }

    fun getGender(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[GENDER_KEY] }

}
