package com.example.cinesphere.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinesphere.domain.model.Media
import com.example.cinesphere.domain.model.Movie

@Composable
fun MediaCard(
    media: Media,
    onAddToWishlist: (Media) -> Unit,
    modifier: Modifier = Modifier,
    isWishlisted: Boolean = false
) {
    Box(
        modifier = modifier
            .width(150.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = media.posterUrl,
            contentDescription = media.title,
            modifier = Modifier
                .width(150.dp)
                .height(225.dp),
            contentScale = ContentScale.Crop,
        )
        // Immersive Gradient Overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                        startY = 100f // Start slightly earlier to ensure text bg is opaque enough
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {
            Text(
                text = media.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (media is Movie && !media.releaseDate.isNullOrBlank() && media.releaseDate!!.length >= 4) {
                Text(
                    text = media.releaseDate!!.substring(0, 4),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
        IconButton(
            onClick = { onAddToWishlist(media) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Add to Wishlist",
                tint = if (isWishlisted) Color.Red else Color.White // Keep White for top icon over image
            )
        }
    }
}