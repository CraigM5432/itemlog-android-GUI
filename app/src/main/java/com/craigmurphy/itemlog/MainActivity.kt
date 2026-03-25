package com.craigmurphy.itemlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.craigmurphy.itemlog.ui.navigation.AppNavGraph
import com.craigmurphy.itemlog.ui.theme.ItemLogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItemLogTheme {
                AppNavGraph()
            }
        }
    }
}