package com.example.cinesphere.presentation.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinesphere.domain.model.MediaType

@Composable
fun FilterPanel(
    state: DiscoverState,
    onMediaTypeSelected: (MediaType) -> Unit,
    onGenreSelected: (Int) -> Unit,
    onClearFilters: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Filter Options")
            Button(onClick = onClearFilters) {
                Text(text = "Clear all")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Media Type selection
        Text(text = "Media Type")
        Row(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = state.selectedMediaType == MediaType.MOVIE,
                    onClick = { onMediaTypeSelected(MediaType.MOVIE) }
                )
                Text(text = "Movies")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = state.selectedMediaType == MediaType.WEB_SERIES,
                    onClick = { onMediaTypeSelected(MediaType.WEB_SERIES) }
                )
                Text(text = "TV Shows")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Genre selection
        Text(text = "Genres")
        val genres = if (state.selectedMediaType == MediaType.MOVIE) state.movieGenres else state.tvGenres
        LazyColumn {
            items(genres) { genre ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = state.selectedGenreIds.contains(genre.id),
                        onCheckedChange = { onGenreSelected(genre.id) }
                    )
                    Text(text = genre.name)
                }
            }
        }
    }
}