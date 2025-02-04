package uz.webase.moviecleanarch.ui.feed

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

import kotlinx.coroutines.flow.flowOf
import uz.webase.moviecleanarch.util.collectAsEffect

/**
 * @author by Ali Asadi on 18/04/2023
 */

@Composable
fun FeedPage(
    mainRouter: MainRouter,
    viewModel: FeedViewModel,
    sharedViewModel: NavigationBarSharedViewModel,
) {
    val moviesPaging = viewModel.movies.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullRefreshState(uiState.showLoading, { viewModel.onRefresh() })
    val lazyGridState = rememberLazyGridState()

    viewModel.navigationState.collectAsEffect { navigationState ->
        when (navigationState) {
            is MovieDetails -> mainRouter.navigateToMovieDetails(navigationState.movieId)
        }
    }
    viewModel.refreshListState.collectAsEffect {
        moviesPaging.refresh()
    }

    sharedViewModel.bottomItem.collectAsEffect {
        if (it.page == Page.Feed) {
            lazyGridState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(key1 = moviesPaging.loadState) {
        viewModel.onLoadStateUpdate(moviesPaging.loadState)
    }

    PullToRefresh(state = pullToRefreshState, refresh = uiState.showLoading) {
        FeedScreen(
            movies = moviesPaging,
            uiState = uiState,
            lazyGridState = lazyGridState,
            onMovieClick = viewModel::onMovieClicked
        )
    }
}

@Composable
private fun FeedScreen(
    movies: LazyPagingItems<MovieListItem>,
    uiState: FeedUiState,
    lazyGridState: LazyGridState,
    onMovieClick: (movieId: Int) -> Unit
) {
    Surface {
        if (uiState.showLoading) {
            LoaderFullScreen()
        } else {
            MovieList(movies, onMovieClick, lazyGridState)
        }
    }
}

@Preview(device = Devices.PIXEL_3, showSystemUi = true)
@Composable
private fun FeedScreenPreview() {
    val movies = flowOf(
        PagingData.from(
            listOf<MovieListItem>(
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
            )
        )
    ).collectAsLazyPagingItems()
    PreviewContainer {
        FeedScreen(
            movies = movies,
            uiState = FeedUiState(
                showLoading = false,
                errorMessage = null,
            ),
            lazyGridState = rememberLazyGridState(),
            onMovieClick = {}
        )
    }
}