package uz.webase.moviecleanarch.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.webase.data.util.NetworkMonitorImpl
import uz.webase.moviecleanarch.di.qualifiers.AppSettingsSharedPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @AppSettingsSharedPreference
    fun provideAppSettingsSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitorImpl = NetworkMonitorImpl(context)
}