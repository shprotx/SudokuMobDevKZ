package ru.shprot.sudokumobdevkz.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.shprot.sudokumobdevkz.BuildConfig
import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardCfApi
import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardCfApiHolder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardCfModule {

    @Provides
    @Singleton
    fun provideLeaderboardCfApiHolder(client: OkHttpClient, json: Json): LeaderboardCfApiHolder {
        val url = BuildConfig.LEADERBOARD_CF_URL.ifEmpty { return LeaderboardCfApiHolder(null) }
        val normalizedUrl = if (url.endsWith("/")) url else "$url/"
        val api = Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(LeaderboardCfApi::class.java)
        return LeaderboardCfApiHolder(api)
    }
}