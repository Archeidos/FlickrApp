package com.xylematic.flickrapp.di

import com.xylematic.flickrapp.api.FlickrService
import com.xylematic.flickrapp.data.FlickrRepository
import com.xylematic.flickrapp.data.FlickrRepositoryImpl
import com.xylematic.flickrapp.viewmodel.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<FlickrService> { FlickrService.create() }
    single<FlickrRepository> { FlickrRepositoryImpl(get()) }

    viewModel { HomeViewModel(flickrRepository = get()) }
}

val networkModule = module {
    single { provideRetrofit() }
}

fun provideRetrofit(): Retrofit {
    val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api.flickr.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

val flickrModules = listOf(appModule, networkModule)