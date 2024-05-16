package com.zhulin.fakedet.business.usecases

import com.zhulin.fakedet.data.stores.SettingsStore
import com.zhulin.fakedet.business.models.Language
import com.zhulin.fakedet.business.repositories.StringResourceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StringResourceHandler @Inject constructor(
    private val resourceRepository: StringResourceRepository,
    private val settingsStore: SettingsStore,
) {
    suspend operator fun invoke(id: Int): String =
        resourceRepository.get(id, Language.from(settingsStore.getLanguage.first()))
}