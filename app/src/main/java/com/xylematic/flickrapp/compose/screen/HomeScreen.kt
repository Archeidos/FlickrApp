package com.xylematic.flickrapp.compose.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.xylematic.flickrapp.R
import com.xylematic.flickrapp.data.PhotoItem
import com.xylematic.flickrapp.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPhotoClicked: (PhotoItem) -> Unit = {},
) {

    val viewModel: HomeViewModel = koinViewModel()
    val showDialog = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf(viewModel.searchText.value) }

    val photos by viewModel.photos.collectAsState(initial = listOf())  // Collecting photos as state
    val isLoading by viewModel.isLoading.collectAsState()

    val activity = (LocalContext.current as Activity)

    Scaffold(
        topBar = {
            HomeTopBar(
                showDialog = showDialog,
                searchText = searchText
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
    if (showDialog.value) {
        SearchDialog(showDialog, searchText, viewModel)
    }
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    showDialog: MutableState<Boolean>,
    searchText: MutableState<String>
) {
    CenterAlignedTopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.cd_search)
                )
            }
        }
    )
}

@Composable
fun SearchDialog(
    showDialog: MutableState<Boolean>,
    searchText: MutableState<String>,
    viewModel: HomeViewModel
) {
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            Text(text = "Search")
        },
        text = {
            OutlinedTextField(
                value = searchText.value,
                onValueChange = { newText ->
                    searchText.value = newText
                    viewModel.updateSearchResults(newText)
                },
                label = { Text("Type here...") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { showDialog.value = false }
            ) {
                Text("Close")
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
