package uz.webase.moviecleanarch.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.webase.domain.util.NetworkMonitor
import uz.webase.moviecleanarch.di.qualifiers.AppSettingsSharedPreference
import uz.webase.moviecleanarch.ui.theme.AppTheme
import uz.webase.moviecleanarch.ui.theme.MovieCleanArchTheme
import uz.webase.moviecleanarch.ui.widget.NoInternetConnectionBanner
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val DARK_MODE = "dark_mode"
    }

    @Inject
    @AppSettingsSharedPreference
    lateinit var appSettings: SharedPreferences

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private fun isDarkModeEnabled() = appSettings.getBoolean(DARK_MODE, false)

    private fun enableDarkMode(enable: Boolean) =
        appSettings.edit().putBoolean(DARK_MODE, enable).commit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var darkMode by remember { mutableStateOf(isDarkModeEnabled()) }

            AppTheme(darkTheme = darkMode) {
                Column {
                    val networkStatus by networkMonitor.networkState.collectAsState(null)

                    networkStatus?.let {
                        if (it.isOnline.not()) {
                            NoInternetConnectionBanner()
                        }
                    }

                    MainGraph(
                        mainNavController = navController,
                        darkMode = darkMode,
                        onThemeUpdated = {
                            val updated = !darkMode
                            enableDarkMode(updated)
                            darkMode = updated
                        }
                    )
                }
            }
        }
    }
}
