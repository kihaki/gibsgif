package de.koandesign.gibsgif.api.gfycat

import de.koandesign.gibsgif.api.gfycat.entity.GfycatErrorResponse
import de.koandesign.gibsgif.api.gfycat.entity.search.GfycatSearchResponse
import de.koandesign.gibsgif.api.gfycat.entity.trending.GfycatTrendingResponse
import de.koandesign.gibsgif.api.gfycat.service.GfycatService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import java.io.IOException

class GfycatApi(private val service: GfycatService) {

    class ResponseException(message: String) : IOException(message) {
        constructor(errorBody: ResponseBody?) : this(errorBody?.toString()
            ?.let { Json.decodeFromString<GfycatErrorResponse>(it).errorMessage.description }
            ?: "Unknown error")
    }

    suspend fun search(query: String, count: Int, cursor: String?): GfycatSearchResponse =
        service.search(query, count, cursor).run {
            body() ?: throw ResponseException(errorBody())
        }

    suspend fun getTrending(count: Int, cursor: String?): GfycatTrendingResponse =
        service.getTrending(count, cursor).run {
            body() ?: throw ResponseException(errorBody())
        }
}
