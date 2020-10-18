package de.koandesign.gibsgif.search.usecase

import androidx.annotation.VisibleForTesting
import de.koandesign.gibsgif.search.repo.FeedRepository
import de.koandesign.gibsgif.search.repo.entity.Media
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.Delegates

class GetKeywordedFeedUseCase(
    private val feedRepository: FeedRepository,
    private val scope: CoroutineScope
) {
    @VisibleForTesting
    companion object {
        const val PAGE_SIZE = 30
    }

    private var nextPageCursor: String? = null
    private var currentQuery: String? = null
    var query: String? by Delegates.observable(null) { _, old, new ->
        nextPageCursor = null
        loadMoreJob.cancel()
    }
    private var loadMoreJob: Job = Job().apply { complete() }

    private val _gifs: MutableStateFlow<List<Media>> = MutableStateFlow(listOf())
    val gifs: Flow<List<Media>> = _gifs

    fun loadMore() {
        if (loadMoreJob.isCompleted || loadMoreJob.isCancelled) {
            loadMoreJob = scope.launch {
                runCatching { fetchNext() }
                    .onSuccess { nextPage ->
                        nextPageCursor = nextPage.nextCursor
                        if (currentQuery != query) {
                            currentQuery = query
                            _gifs.value = nextPage.items
                        } else {
                            _gifs.value = _gifs.value + nextPage.items
                        }
                    }
                    .onFailure { error ->
                        Timber.e(error)
                        // TODO: Show error to user if necessary
                    }
            }
        }
    }

    private suspend fun fetchNext(): FeedRepository.FeedPage = if (query == null) {
        feedRepository.getTrending(count = PAGE_SIZE, cursor = nextPageCursor)
    } else {
        feedRepository.search(query = query!!, count = PAGE_SIZE, cursor = nextPageCursor)
    }
}
