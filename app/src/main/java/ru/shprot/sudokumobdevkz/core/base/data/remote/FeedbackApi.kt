package ru.shprot.sudokumobdevkz.core.base.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedbackApi {

    @POST("submitFeedback")
    suspend fun submit(
        @Body body: FeedbackRequestDto,
    ): Response<Unit>
}