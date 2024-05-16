package com.zhulin.fakedet.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppWhite

@Composable
fun BottomApplicationBar(
    onBotClick: () -> Unit,
    botSelected: Boolean,
    onTGClick: () -> Unit,
    tgSelected: Boolean,
    onNewsClick: () -> Unit,
    newsSelected: Boolean
) {
    BottomAppBar(
        containerColor = AppWhite,
        actions = {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                item {
                    IconButton(onClick = onBotClick) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_bottombar_bot
                            ),
                            contentDescription = "",
                            tint = if (botSelected) AppBlue else AppBlack,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(96.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                }

                item {
                    IconButton(onClick = onTGClick) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_telegram
                            ),
                            contentDescription = "",
                            tint = if (tgSelected) AppBlue else AppBlack,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(96.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                }

                item {
                    IconButton(onClick = onNewsClick) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_news
                            ),
                            contentDescription = "",
                            tint = if (newsSelected) AppBlue else AppBlack,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(96.dp)
                        )
                    }
                }
            }
        },
    )
}
