package com.xylematic.flickrapp.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.xylematic.flickrapp.R
import com.xylematic.flickrapp.data.PhotoItem
import com.xylematic.flickrapp.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPhotoClicked: (PhotoItem) -> Unit = {},
) {

    val viewModel: HomeViewModel = koinViewModel()
    val searchText = remember { mutableStateOf(viewModel.searchText.value) }
    var isSearchBoxVisible by remember { mutableStateOf(false) }

    val photos by viewModel.photos.collectAsState(initial = listOf())  // Collecting photos as state
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(
                searchText = searchText,
                viewModel = viewModel,
                isSearchBoxVisible = isSearchBoxVisible,
                onSearchIconClick = { isSearchBoxVisible = !isSearchBoxVisible }
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(innerPadding),
            columns = GridCells.Fixed(count = 2)
        ) {
            items(photos) { photo ->
                PhotoItemView(photo, onPhotoClicked)
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp).align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    viewModel: HomeViewModel,
    searchText: MutableState<String>,
    isSearchBoxVisible: Boolean,
    onSearchIconClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(
                    visible = isSearchBoxVisible,
                    enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    TextField(
                        value = searchText.value,
                        onValueChange = { newText ->
                            searchText.value = newText
                            viewModel.updateSearchResults(newText)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search...") },
                        singleLine = true
                    )
                }
                AnimatedVisibility(
                    visible = !isSearchBoxVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(stringResource(id = R.string.app_name), modifier = Modifier.weight(1f))
                }
            }
        },
        actions = {
            IconButton(onClick = onSearchIconClick) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun PhotoItemView(photo: PhotoItem, onPhotoClicked: (PhotoItem) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        onClick = {
            onPhotoClicked(photo)
        }
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(photo.media.m),
                contentDescription = photo.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(150.dp)
            )
            Text(
                text = photo.title,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
    }
}
