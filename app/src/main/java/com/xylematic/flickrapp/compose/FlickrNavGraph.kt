package com.xylematic.flickrapp.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xylematic.flickrapp.compose.screen.HomeScreen
import com.xylematic.flickrapp.compose.screen.PhotoDetailScreen

@Composable
fun FlickrNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Routing.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = Routing.Home.route
        ) { navBackStackEntry ->
            HomeScreen(onPhotoClicked = {
                navController.navigate(
                    Routing.PhotoDetail.createRoute(
                        title = it.title,
                        imageUrl = it.media.m,
                        desc = it.description,
                        author = it.author,
                        publishedDate = it.published
                    ),
                )
            })
        }
        composable(
            route = Routing.PhotoDetail.route,
            arguments = Routing.PhotoDetail.navArgs
        ) { navBackStackEntry ->
            val title: String = navBackStackEntry.arguments?.getString("title") ?: ""
            val imageUrl: String = navBackStackEntry.arguments?.getString("imageUrl") ?: ""
            val desc: String = navBackStackEntry.arguments?.getString("desc") ?: ""
            val author: String = navBackStackEntry.arguments?.getString("author") ?: ""
            val publishedDate: String =
                navBackStackEntry.arguments?.getString("publishedDate") ?: ""
            PhotoDetailScreen(
                title = title,
                imageUrl = imageUrl,
                desc = desc,
                author = author,
                publishedDate = publishedDate
            )
        }
        // Other composable screens here
    }

}