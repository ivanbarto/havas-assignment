package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositoryDetail

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ivanbartolelli.kotlinrepos.R
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    navController: NavController,
    post: Post
) {

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .scrollable(scrollState, Orientation.Vertical)
    ) {
        TopAppBar(
            title = {},
            colors = TopAppBarColors(
                containerColor = Color.Transparent,
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified,
                Color.Unspecified
            ),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = stringResource(R.string.text_return),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(10.dp)
                    )
                }
            }
        )

        Text(
            text = "repository.name",
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )


    }

}