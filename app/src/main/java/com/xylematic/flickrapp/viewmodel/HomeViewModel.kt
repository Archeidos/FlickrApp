package com.xylematic.flickrapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xylematic.flickrapp.api.FlickrService
import com.xylematic.flickrapp.data.FlickrRepository
import com.xylematic.flickrapp.data.FlickrRepositoryImpl
import com.xylematic.flickrapp.data.PhotoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    // Replace with DI injection
    defaultSearchText: String = "porcupine",
    private var flickrRepository: FlickrRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _photos = MutableStateFlow<List<PhotoItem>?>(null)
    val photos: Flow<List<PhotoItem>> get() = _photos.filterNotNull()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        updateSearchResults(defaultSearchText)
    }

    fun updateSearchResults(searchText: String) = viewModelScope.launch {
        _isLoading.value = true
        _photos.value = flickrRepository.searchPhotos(searchText).getOrNull()
        _isLoading.value = false
    }

}