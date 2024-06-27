package com.xylematic.flickrapp.data

interface FlickrRepository {

    suspend fun searchPhotos(searchText: String): Result<List<PhotoItem>>

}