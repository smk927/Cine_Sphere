package com.example.cinesphere.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinesphere.domain.model.Media

@Composable
fun MediaListItem(media: Media, onMediaClick: (Int, String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onMediaClick(media.id, media.mediaType.name) }
    ) {
        Column {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${media.posterUrl}",
                contentDescription = media.title,
                modifier = Modifier.aspectRatio(0.7f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = media.title,
                modifier = Modifier.padding(8.dp),
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
