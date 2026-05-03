package ru.shprot.sudokumobdevkz.model.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

@Serializable
data class FirebaseStatDto(
    val averageTime: Int = 0,
    val bestTime: Int = 0,
    val gamesWon: Int = 0,
    val gamesStarted: Int = 0,
    val winsWithoutErrors: Int = 0,
    val bestWinsLine: Int = 0,
)

interface FirebaseApi {

    @PUT("stats/{deviceId}/{difficulty}.json")
    suspend fun uploadStatistic(
        @Path("deviceId") deviceId: String,
        @Path("difficulty") difficulty: Int,
        @Body stat: FirebaseStatDto,
    )

    @GET("stats.json")
    suspend fun getAllStats(): Map<String, Map<String, FirebaseStatDto>>?
}
