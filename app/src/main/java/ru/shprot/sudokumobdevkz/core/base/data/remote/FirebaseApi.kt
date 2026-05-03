package ru.shprot.sudokumobdevkz.core.base.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FirebaseApi {

    @PUT("stats/{deviceId}/{difficulty}.json")
    suspend fun uploadStatistic(
        @Path("deviceId") deviceId: String,
        @Path("difficulty") difficulty: Int,
        @Body stat: FirebaseStatDto,
    )

    @GET("stats.json")
    suspend fun getAllStats(): Map<String, Map<String, FirebaseStatDto>>?

    @PUT("crashes/{deviceId}/{timestamp}.json")
    suspend fun uploadCrash(
        @Path("deviceId") deviceId: String,
        @Path("timestamp") timestamp: String,
        @Body crash: CrashDto,
    )
}
