package com.atiq.neugelb.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.atiq.neugelb.ui.view.main.MainScreenContainer
import com.atiq.neugelb.ui.view.movie_details.MovieDetailScreenContainer
import com.atiq.neugelb.ui.view.search_movies.SearchMovieScreenContainer

fun NavGraphBuilder.HomeNavigation(controller: NavHostController) {
    navigation(Screens.Main.route, HomeNavigationRoute) {
        composable(route = Screens.Main.route) {
            MainScreenContainer(controller = controller)
        }

        composable(route = Screens.Detail.route + "/{$ArgumentMovieId}",
            arguments = listOf(
                navArgument(ArgumentMovieId) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getLong(ArgumentMovieId) ?: error("$ArgumentMovieId missing")
            MovieDetailScreenContainer(movieId = movieId, controller = controller)
        }

        composable(route = Screens.Search.route) {
            SearchMovieScreenContainer(controller = controller)
        }
    }
}