package com.zhulin.fakedet.ui.helpers

import com.zhulin.fakedet.R
import fake.detection.post.bridge.api.Bridge.Site

object SiteHelpers {
    data class SiteInfo(val type: Site, val logoId: Int, val nameId: Int)

    fun getSites() =
        listOf(
            SiteInfo(Site.LentaRu, R.drawable.lenta_logo, R.string.site_lentaru),
            SiteInfo(Site.IzRu, R.drawable.iz_logo, R.string.site_iz),
            SiteInfo(Site.Interfax, R.drawable.interfax_logo, R.string.site_interfax),
            SiteInfo(Site.Ria, R.drawable.ria_logo, R.string.site_ria),
            SiteInfo(Site.Tass, R.drawable.tass_logo, R.string.site_tass),
            SiteInfo(Site.CNN, R.drawable.cnn_logo, R.string.site_cnn),
            SiteInfo(Site.BBC, R.drawable.bbc_logo, R.string.site_bbc),
        )

}