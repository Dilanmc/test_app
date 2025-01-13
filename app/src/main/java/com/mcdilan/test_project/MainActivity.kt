package com.mcdilan.test_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mcdilan.test_project.ui.screens.QuotesScreen
import com.mcdilan.test_project.ui.theme.Test_projectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test_projectTheme {
                QuotesScreen()
            }
        }
    }
}

