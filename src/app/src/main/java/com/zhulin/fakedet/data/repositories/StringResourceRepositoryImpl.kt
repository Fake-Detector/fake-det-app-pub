package com.zhulin.fakedet.data.repositories

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.zhulin.fakedet.business.models.Language
import com.zhulin.fakedet.business.repositories.StringResourceRepository
import java.util.Locale

class StringResourceRepositoryImpl(private val application: Application) :
    StringResourceRepository {
    override fun get(id: Int, language: Language): String {
        val locale = Locale(language.language)
        val resource = getLocalizedResources(application, locale)

        return resource?.getString(id)
            ?: throw Resources.NotFoundException("The element with current id does not exit.")
    }

    private fun getLocalizedResources(context: Context, locale: Locale): Resources? {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        val localizedContext = context.createConfigurationContext(configuration)

        return localizedContext.resources
    }
}