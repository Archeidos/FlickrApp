package com.xylematic.flickrapp.data

import com.xylematic.flickrapp.api.FlickrService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlickrRepositoryImpl(val flickrService: FlickrService) : FlickrRepository {

    override suspend fun searchPhotos(searchText: String): Result<List<PhotoItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val photoItemList = flickrService.getPhotos(
                    format = "json",
                    nojsoncallback = 1,
                    tags = searchText
                ).items
                Result.success(photoItemList)
            } catch (e: Exception) {
                // Should ideally return results to UI to display errors
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

}