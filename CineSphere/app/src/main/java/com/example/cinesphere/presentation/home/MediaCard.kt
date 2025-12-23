package com.example.cinesphere.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinesphere.domain.model.Media

@Composable
fun MediaCard(
    media: Media,
    onAddToWishlist: (Media) -> Unit,
    modifier: Modifier = Modifier,
    isWishlisted: Boolean = false
) {
    Card(modifier = modifier.width(150.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                AsyncImage(
                    model = media.posterUrl,
                    contentDescription = media.title,
                    modifier = Modifier
                        .width(150.dp)
                        .height(225.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { onAddToWishlist(media) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) androidx.compose.material.icons.Icons.Filled.Favorite else androidx.compose.material.icons.Icons.Filled.FavoriteBorder,
                        contentDescription = "Add to Wishlist",
                        tint = if (isWishlisted) Color.Red else Color.White
                    )
                }
            }
            Text(
                text = media.title,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
