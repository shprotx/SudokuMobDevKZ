package ru.shprot.sudokumobdevkz.core.base.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LeaderboardCfApi {

    @POST("submitLeaderboard")
    suspend fun submit(
        @Body body: LeaderboardSubmitDto,
    ): Response<Unit>

    @POST("backfillLeaderboard")
    suspend fun backfill(
        @Body body: LeaderboardBackfillDto,
    ): Response<Unit>
}