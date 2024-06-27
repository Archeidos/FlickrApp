package com.xylematic.flickrapp.compose

import android.net.Uri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Routing(
    val route: String,
    val navArgs: List<NamedNavArgument> = emptyList()
) {

    data object Home : Routing("home")

    data object PhotoDetail : Routing(
        route = "photoDetail/{title}/{imageUrl}/{desc}/{author}/{publishedDate}",
        navArgs = listOf(
            navArgument("title") {
                type = NavType.StringType
            },
            navArgument("imageUrl") {
                type = NavType.StringType
            },
            navArgument("desc") {
                type = NavType.StringType
            },
            navArgument("author") {
                type = NavType.StringType
            },
            navArgument("publishedDate") {
                type = NavType.StringType
            }
        )
    ) {
        fun createRoute(
            title: String,
            imageUrl: String,
            desc: String,
            author: String,
            publishedDate: String
        ) =
            "photoDetail/${Uri.encode(title)}/${Uri.encode(imageUrl)}" +
                    "/${Uri.encode(desc)}/${author}/${Uri.encode(publishedDate)}"
    }

}