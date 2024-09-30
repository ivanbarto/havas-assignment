package com.ivanbartolelli.assignment.features.posts.presentation.postDetail

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun textColor() = if (isSystemInDarkTheme()) {
    Color(0xFFFFFFFF)
} else {
    Color(0xFF000000)
}
