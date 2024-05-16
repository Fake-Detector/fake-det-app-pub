package com.zhulin.fakedet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.zhulin.fakedet.ui.navigation.Navigation
import com.zhulin.fakedet.ui.theme.FakeDetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FakeDetTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Navigation(
                        navController = rememberNavController(),
                    )
                }
            }
        }
    }
}

