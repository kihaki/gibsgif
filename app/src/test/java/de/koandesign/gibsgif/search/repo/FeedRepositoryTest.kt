package de.koandesign.gibsgif.search.repo

import de.koandesign.gibsgif.api.gfycat.GfycatApi
import de.koandesign.gibsgif.api.gfycat.entity.GfycatResponse
import de.koandesign.gibsgif.api.gfycat.entity.search.GfycatSearchResponse
import de.koandesign.gibsgif.api.gfycat.entity.trending.GfycatTrendingResponse
import de.koandesign.gibsgif.search.repo.converter.Converter
import de.koandesign.gibsgif.search.repo.entity.Media
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FeedRepositoryTest {

    val api: GfycatApi = mockk()
    val mediaListConverter: Converter<List<GfycatResponse>, List<Media>> = mockk()
    val feedRepository = FeedRepository(api, mediaListConverter)

    @Test
    fun `calls search with query, count and cursor`() = runBlocking {
        coEvery { api.search(any(), any(), any()) } returns GfycatSearchResponse(
            "cursor",
            null,
            listOf(),
            null
        )
        every { mediaListConverter.invoke(any()) } returns listOf()
        val query = "query"
        val count = 5
        val cursor = "cursor"
        feedRepository.search(query, count, cursor)
        coVerify(exactly = 1) { api.search(query, count, cursor) }
        verify(exactly = 1) { mediaListConverter.invoke(listOf()) }
    }

    @Test
    fun `calls trending with count`() = runBlocking {
        coEvery { api.getTrending(any(), any()) } returns GfycatTrendingResponse(
            "cursor",
            null,
            listOf(),
            null
        )
        every { mediaListConverter.invoke(any()) } returns listOf()
        val count = 5
        val cursor = "cursor"
        feedRepository.getTrending(count, cursor)
        coVerify(exactly = 1) { api.getTrending(count, cursor) }
        verify(exactly = 1) { mediaListConverter.invoke(listOf()) }
    }

}
