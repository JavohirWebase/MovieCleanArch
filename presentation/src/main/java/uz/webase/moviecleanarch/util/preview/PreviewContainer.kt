package uz.webase.moviecleanarch.util.preview

import androidx.compose.runtime.Composable
import uz.webase.moviecleanarch.ui.theme.AppTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}