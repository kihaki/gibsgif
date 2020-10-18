package de.koandesign.gibsgif.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.koandesign.gibsgif.search.repo.FeedRepository
import de.koandesign.gibsgif.search.repo.entity.Media
import de.koandesign.gibsgif.search.usecase.GetKeywordedFeedUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(feedRepo: FeedRepository) : ViewModel() {

    private val getFeedGifsUseCase = GetKeywordedFeedUseCase(feedRepo, viewModelScope)
        .apply { loadMore() }

    data class State(
        val media: List<Media>,
        val isAutoScrolling: Boolean,
        val isSearchInputActive: Boolean
    )

    private var currentState: State
        get() = _state.value
        set(value) {
            _state.value = value
        }

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State(
            media = listOf(),
            isAutoScrolling = true,
            isSearchInputActive = false
        )
    )
    val state: StateFlow<State> = _state

    private val currentUserQuery = ConflatedBroadcastChannel<String?>()
    private var startDelayedAutoscrolling: Job? = null

    init {
        viewModelScope.launch {
            currentUserQuery.asFlow()
                .distinctUntilChanged()
                .debounce(1000)
                .collect { query ->
                    getFeedGifsUseCase.query = query
                    getFeedGifsUseCase.loadMore()
                }
        }
        viewModelScope.launch {
            getFeedGifsUseCase.gifs.collect { newGifs ->
                currentState = currentState.copy(media = newGifs)
            }
        }
    }

    fun onEndOfFeedReached() {
        getFeedGifsUseCase.loadMore()
    }

    fun onSearchInputChange(query: String) {
        currentUserQuery.offer(query.let { if (it.isBlank()) null else it })
    }

    fun onSearchInputEnterClicked() {
        getFeedGifsUseCase.query = currentUserQuery.valueOrNull
        getFeedGifsUseCase.loadMore()
    }

    fun onUserFinishScrolling() {
        startDelayedAutoscrolling = viewModelScope.launch {
            delay(2000)
            startAutoScrolling()
        }
    }

    fun onUserStartScrolling() {
        startDelayedAutoscrolling?.cancel()
        currentState = currentState.copy(isAutoScrolling = false)
    }

    private fun startAutoScrolling() {
        currentState = currentState.copy(isAutoScrolling = true)
    }

    fun onSearchCardClick() {
        currentState = currentState.copy(isSearchInputActive = true)
    }

    fun onBackPressed() {
        if (currentState.isSearchInputActive) {
            currentState = currentState.copy(isSearchInputActive = false)
        }
    }
}
