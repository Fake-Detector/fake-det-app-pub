package com.zhulin.fakedet.ui.screens.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.models.ItemInfo
import com.zhulin.fakedet.business.models.ItemPostType
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getTrustBackgroundColor
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppWhite

@Composable
fun BottomBarCustom(
    trustValue: Int,
    isChecked: Boolean,
    items: List<ItemInfo>,
    onItemClick: (itemId: String) -> Unit,
) {
    BottomAppBar(
        containerColor = if (isChecked) trustValue.getTrustBackgroundColor() else AppWhite,
        actions = {
            LazyRow (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items) {
                    IconButton(onClick = { onItemClick(it.id) }) {
                        Icon(
                            painter = painterResource(
                                id = when (it.type) {
                                    ItemPostType.TEXT -> R.drawable.ic_text
                                    ItemPostType.PHOTO -> R.drawable.ic_image
                                    ItemPostType.VIDEO -> R.drawable.ic_video
                                    ItemPostType.AUDIO -> R.drawable.ic_audio
                                    ItemPostType.NONE -> R.drawable.ic_attach
                                }
                            ),
                            contentDescription = "",
                            tint = AppBlack,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(48.dp)
                        )
                    }
                }
            }
        })
}
