package com.zhulin.fakedet.data.stores

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zhulin.fakedet.data.helpers.JWTHelpers.getJWTClaim
import kotlinx.coroutines.flow.map

class UserStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
    }

    val getName = context.dataStore.data.map {
        it[NAME] ?: ""
    }

    val getLogin = context.dataStore.data.map {
        (it[TOKEN] ?: "").getJWTClaim()
    }

    val getToken = context.dataStore.data.map {
        it[TOKEN] ?: ""
    }

    suspend fun update(name: String, token: String) {
        context.dataStore.edit {
            it[NAME] = name
            it[TOKEN] = token
        }
    }
}