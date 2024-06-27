package com.xylematic.flickrapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xylematic.flickrapp.data.FlickrRepository
import com.xylematic.flickrapp.data.PhotoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

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

    /** Ideally would return results to UI for error messaging **/
    fun updateSearchResults(searchText: String) = viewModelScope.launch {
        _isLoading.value = true
        _photos.value = flickrRepository.searchPhotos(searchText).getOrNull()
        _isLoading.value = false
    }

}