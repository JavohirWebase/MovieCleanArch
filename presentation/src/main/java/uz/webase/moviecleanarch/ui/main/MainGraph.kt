package uz.webase.moviecleanarch.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import uz.webase.moviecleanarch.navigation.Graph
import uz.webase.moviecleanarch.navigation.Page
import uz.webase.moviecleanarch.util.composableHorizontalSlide

@Composable
fun MainGraph(
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    NavHost(
        navController = mainNavController,
        startDestination = Page.NavigationBar,
        route = Graph.Main::class
    ) {
        composableHorizontalSlide<Page.NavigationBar> { backStack ->
            val nestedNavController = rememberNavController()
            NavigationBarScreen(
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController),
                mainRouter = MainRouter(mainNavController),
                darkMode = darkMode,
                onThemeUpdated = onThemeUpdated,
                nestedNavController = nestedNavController
            ) {
                NavigationBarNestedGraph(
                    navController = nestedNavController,
                    mainNavController = mainNavController,
                    parentRoute = Graph.Main::class
                )
            }
        }

        composableHorizontalSlide<Page.Search> {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchPage(
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }

        composableHorizontalSlide<Page.MovieDetails> {
            val viewModel = hiltViewModel<MovieDetailsViewModel>()
            MovieDetailsPage(
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }
    }
}