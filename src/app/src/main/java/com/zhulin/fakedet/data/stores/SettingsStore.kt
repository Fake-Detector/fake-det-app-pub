package com.zhulin.fakedet.data.stores

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zhulin.fakedet.business.models.Language
import kotlinx.coroutines.flow.map

class SettingsStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val LANGUAGE = stringPreferencesKey("language")
    }

    val getLanguage = context.dataStore.data.map {
        it[LANGUAGE] ?: Language.EN.language
    }

    suspend fun updateLanguage(language: String) {
        context.dataStore.edit {
            it[LANGUAGE] = language
        }
    }
}