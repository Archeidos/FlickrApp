package com.xylematic.flickrapp.api

import com.xylematic.flickrapp.data.FlickrPhotosResponse
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface FlickrService {

    @GET("services/feeds/photos_public.gne")
    suspend fun getPhotos(
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: Int,
        @Query("tags") tags: String
    ): FlickrPhotosResponse

    companion object {
        fun create(): FlickrService {
            val retrofit: Retrofit by inject(Retrofit::class.java)
            return retrofit.create(FlickrService::class.java)
        }
    }
}