package de.koandesign.gibsgif.search.repo

import de.koandesign.gibsgif.api.gfycat.GfycatApi
import de.koandesign.gibsgif.api.gfycat.entity.GfycatResponse
import de.koandesign.gibsgif.search.repo.converter.Converter
import de.koandesign.gibsgif.search.repo.entity.Media

class FeedRepository(
    private val api: GfycatApi,
    private val mediaListConverter: Converter<List<GfycatResponse>, List<Media>>
) {
    data class FeedPage(
        val nextCursor: String,
        val items: List<Media>
    )

    suspend fun search(
        query: String,
        count: Int,
        cursor: String? = null
    ): FeedPage {
        val searchResponse = api.search(query, count, cursor)
        return FeedPage(searchResponse.cursor, mediaListConverter(searchResponse.gfycats))
    }

    suspend fun getTrending(count: Int, cursor: String? = null): FeedPage {
        val trendingResponse = api.getTrending(count, cursor)
        return FeedPage(trendingResponse.cursor, mediaListConverter(trendingResponse.gfycats))
    }
}

