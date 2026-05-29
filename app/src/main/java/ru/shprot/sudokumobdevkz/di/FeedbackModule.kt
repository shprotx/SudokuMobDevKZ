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
import ru.shprot.sudokumobdevkz.core.base.data.remote.FeedbackApi
import ru.shprot.sudokumobdevkz.core.base.data.remote.FeedbackApiHolder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedbackModule {

    @Provides
    @Singleton
    fun provideFeedbackApiHolder(client: OkHttpClient, json: Json): FeedbackApiHolder {
        val url = BuildConfig.FEEDBACK_API_URL.ifEmpty { return FeedbackApiHolder(null) }
        val normalizedUrl = if (url.endsWith("/")) url else "$url/"
        val api = Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FeedbackApi::class.java)
        return FeedbackApiHolder(api)
    }
}