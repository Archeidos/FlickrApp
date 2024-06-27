package com.xylematic.flickrapp.data

data class FlickrPhotosResponse(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<PhotoItem>
)

data class PhotoItem(
    val title: String,
    val link: String,
    val media: Media,
    val dateTaken: String,
    val description: String,
    val published: String,
    val author: String,
    val authorId: String,
    val tags: String
)

data class Media(
    val m: String
)