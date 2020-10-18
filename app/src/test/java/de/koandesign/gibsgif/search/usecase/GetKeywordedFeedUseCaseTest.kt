package de.koandesign.gibsgif.search.usecase

import de.koandesign.gibsgif.search.repo.FeedRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Test

class GetKeywordedFeedUseCaseTest {

    val feedRepo: FeedRepository = mockk()
    val testScope = TestCoroutineScope()
    val useCase = GetKeywordedFeedUseCase(feedRepo, testScope)

    @Test
    fun `loads trending initially`() {
        val cursor = "cursor"
        coEvery { feedRepo.getTrending(any(), any()) } returns FeedRepository.FeedPage(
            cursor,
            listOf()
        )
        useCase.loadMore()
        coVerify(exactly = 1) { feedRepo.getTrending(GetKeywordedFeedUseCase.PAGE_SIZE, null) }
    }

    @Test
    fun `uses cursor returned from the previous trending request in next trending request`() {
        val cursor = "cursor"
        coEvery { feedRepo.getTrending(any(), any()) } returns FeedRepository.FeedPage(
            cursor,
            listOf()
        )
        useCase.loadMore()
        coVerify(exactly = 1) { feedRepo.getTrending(GetKeywordedFeedUseCase.PAGE_SIZE, null) }
        useCase.loadMore()
        coVerify(exactly = 1) { feedRepo.getTrending(GetKeywordedFeedUseCase.PAGE_SIZE, cursor) }
    }

    @Test
    fun `loads search when query is specified`() {
        val cursor = "cursor"
        val query = "query"
        coEvery { feedRepo.search(any(), any()) } returns FeedRepository.FeedPage(
            cursor,
            listOf()
        )
        useCase.query = query
        useCase.loadMore()
        coVerify(exactly = 1) { feedRepo.search(query, GetKeywordedFeedUseCase.PAGE_SIZE, null) }
    }

    @Test
    fun `uses cursor returned from the previous search request in next search request`() {
        val cursor = "cursor"
        val query = "query"
        coEvery { feedRepo.search(any(), any()) } returns FeedRepository.FeedPage(
            cursor,
            listOf()
        )
        useCase.query = query
        useCase.loadMore()
        coVerify(exactly = 1) { feedRepo.search(query, GetKeywordedFeedUseCase.PAGE_SIZE, null) }
        useCase.loadMore()
        coVerify(exactly = 1) { feedRepo.search(query, GetKeywordedFeedUseCase.PAGE_SIZE, cursor) }
    }

}
