package com.ivanbartolelli.assignment.features.repositories.presentation.postDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ivanbartolelli.assignment.R
import com.ivanbartolelli.assignment.features.repositories.domain.models.Post

@Composable
fun PostDetailScreen(
    navController: NavController,
    post: Post
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        item {
            TopBar(navController)
            TItle(post)
            Body(post)
            Image(post)
            PostInfo(post)
        }
    }
}

@Composable
private fun PostInfo(post: Post) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(start = 10.dp, bottom = 30.dp)
    ) {
        DataContainer(
            icon = ImageVector.vectorResource(id = R.drawable.ic_arrow_updown),
            data = post.ups.toString(),
            contentDescription = stringResource(id = R.string.text_upvotes)
        )
        DataContainer(
            icon = ImageVector.vectorResource(id = R.drawable.ic_comment),
            data = post.commentsCount.toString(),
            contentDescription = stringResource(id = R.string.text_comments)
        )
    }
}

@Composable
private fun Image(post: Post) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(post.imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.text_post_image),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}

@Composable
private fun Body(post: Post) {
    Text(
        text = post.body,
        color = textColor(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
        textAlign = TextAlign.Start,
    )
}

@Composable
private fun TItle(post: Post) {
    Text(
        text = post.title,
        color = textColor(),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 10.dp),
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(navController: NavController) {
    TopAppBar(
        title = {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_reddit_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(30.dp)
                    .width(90.dp),
                contentScale = ContentScale.FillBounds
            )
        },
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
                    tint = textColor(),
                    contentDescription = stringResource(R.string.text_return),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(10.dp)
                )
            }
        }
    )
}

@Composable
fun DataContainer(icon: ImageVector, data: String, contentDescription: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(
            color = colorResource(id = R.color.url_container),
            shape = RoundedCornerShape(percent = 50)
        )
    ) {
        Icon(
            imageVector = icon,
            tint = textColor(),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(40.dp)
                .padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
        )
        Text(
            text = data,
            color = textColor(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 15.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}