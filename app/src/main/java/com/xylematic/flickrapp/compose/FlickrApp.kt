package com.xylematic.flickrapp.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun FlickrApp() {
    val navController = rememberNavController()
    FlickrAppNavHost(
        navController = navController
    )
}

@Composable
fun FlickrAppNavHost(
    navController: NavHostController
) {
    FlickrNavGraph(navController = navController)
}
