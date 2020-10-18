package de.koandesign.gibsgif.api.gfycat.service

import de.koandesign.gibsgif.api.gfycat.entity.auth.AuthRequest
import de.koandesign.gibsgif.api.gfycat.entity.auth.AuthResponse
import de.koandesign.gibsgif.api.gfycat.entity.search.GfycatSearchResponse
import de.koandesign.gibsgif.api.gfycat.entity.trending.GfycatTrendingResponse
import retrofit2.Response
import retrofit2.http.*

interface GfycatService {

    companion object {
        const val BASE_URL = "https://api.gfycat.com/"
    }

    @Headers(
        value = [
            "Accept: application/json",
            "Content-type:application/json"
        ]
    )
    @POST("/v1/oauth/token")
    suspend fun getAuthToken(@Body request: AuthRequest.ClientCredentials): Response<AuthResponse>

    @GET("/v1/gfycats/trending")
    suspend fun getTrending(
        @Query("count") count: Int,
        @Query("cursor") cursor: String?
    ): Response<GfycatTrendingResponse>

    @GET("/v1/gfycats/search")
    suspend fun search(
        @Query("search_text") query: String,
        @Query("count") count: Int,
        @Query("cursor") cursor: String?
    ): Response<GfycatSearchResponse>
}
