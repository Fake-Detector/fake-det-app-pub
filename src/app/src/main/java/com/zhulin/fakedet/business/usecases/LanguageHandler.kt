package com.zhulin.fakedet.business.usecases

import com.zhulin.fakedet.business.models.Language
import com.zhulin.fakedet.data.stores.SettingsStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LanguageHandler @Inject constructor(
    private val settingsStore: SettingsStore,
) {
    suspend operator fun invoke() {
        val currentLanguage = settingsStore.getLanguage.first()

        settingsStore.updateLanguage(
            if (currentLanguage == Language.RU.language) Language.EN.language
            else Language.RU.language
        )
    }
}