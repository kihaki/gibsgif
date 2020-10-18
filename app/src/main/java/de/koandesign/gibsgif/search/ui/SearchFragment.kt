package de.koandesign.gibsgif.search.ui

import android.animation.TimeAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import coil.load
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import de.koandesign.gibsgif.R
import de.koandesign.gibsgif.search.repo.entity.Media
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_gif.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.max
import kotlin.random.Random


class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModel()

    //    private val loadingFooter by lazy { Section().apply { update(listOf(LoadMoreItem())) } }
    private val gifsSection by lazy { Section() }//.apply { setFooter(loadingFooter) } }
    private val feedAdapter by lazy { GroupAdapter<GroupieViewHolder>().apply { add(gifsSection) } }

    private var autoScroller: TimeAnimator? = null
    private var isRecreated: Boolean = true

    private val endOfFeedWatcher = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                .let { lastVisibleItemPos ->
                    if (lastVisibleItemPos == (feedAdapter.itemCount - 1)) {
                        viewModel.onEndOfFeedReached()
                    }
                }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                SCROLL_STATE_IDLE -> viewModel.onUserFinishScrolling()
                SCROLL_STATE_DRAGGING -> viewModel.onUserStartScrolling()
            }
        }
    }

    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = viewModel.onBackPressed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feed_recycler.adapter = feedAdapter
        val layoutManager = GridLayoutManager(requireContext(), 3)
        feed_recycler.layoutManager = layoutManager
        feedAdapter.spanCount = layoutManager.spanCount
        layoutManager.spanSizeLookup = feedAdapter.spanSizeLookup

        feed_recycler.addOnScrollListener(endOfFeedWatcher)

        search_card.setOnClickListener { viewModel.onSearchCardClick() }
        search_input.doOnTextChanged { _, _, _, _ ->
            viewModel.onSearchInputChange(search_input.text.toString())
        }
        search_input.setOnEditorActionListener { _, actionId, event ->
            if (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)) {
                viewModel.onSearchInputEnterClicked()
            }
            false
        }

        autoScroller = TimeAnimator().apply {
            setTimeListener { _, _, deltaTime ->
                // Normalize auto scrolling speed for all screen sizes
                val screensPerSecond = feed_recycler.height * 0.075f
                val deltaSeconds = deltaTime / 1000f
                feed_recycler.scrollBy(0, max(1, (screensPerSecond * deltaSeconds).toInt()))
            }
        }

        isRecreated = savedInstanceState != null

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                gifsSection.update(state.media.map { MediaItem(it) })
                setAutoScrollingState(state.isAutoScrolling)
                setSearchInputState(state.isSearchInputActive)
                isRecreated = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScroller?.cancel()
        autoScroller = null
    }

    private fun setSearchInputState(isActive: Boolean) {
        backPressCallback.isEnabled = isActive
        if (isActive) {
            search_motion.transitionToState(R.id.search_opened)
            if (isRecreated) search_motion.progress = 1f
            search_card.isClickable = false
        } else {
            search_motion.transitionToState(R.id.search_closed)
            if (isRecreated) search_motion.progress = 0f
            search_card.isClickable = true
        }
    }

    private fun setAutoScrollingState(isActive: Boolean) {
        if (isActive) {
            autoScroller?.start()
        } else {
            autoScroller?.pause()
        }
    }

    private class LoadMoreItem : Item<GroupieViewHolder>() {
        override fun getLayout(): Int = R.layout.item_loading
        override fun isSameAs(other: Item<*>): Boolean = true
        override fun hasSameContentAs(other: Item<*>): Boolean = true

        override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount

        override fun createViewHolder(itemView: View): GroupieViewHolder =
            GroupieViewHolder(itemView)

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        }
    }

    private class MediaItem(private val media: Media) : Item<GroupieViewHolder>() {
        override fun getLayout(): Int = R.layout.item_gif
        override fun isSameAs(other: Item<*>): Boolean =
            other is MediaItem && other.media.url == media.url

        override fun hasSameContentAs(other: Item<*>): Boolean =
            other is MediaItem && other.media == media

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            with(viewHolder.containerView) {
                setBackgroundColor(randomColor())
                item_gif_image.load(media.url)
            }
        }

        override fun getSpanSize(spanCount: Int, position: Int): Int = 1

        override fun createViewHolder(itemView: View): GroupieViewHolder =
            GroupieViewHolder(itemView)

        private fun randomColor() = Color.argb(
            (1.0f * 125).toInt(),
            (Random.nextFloat() * 255).toInt(),
            (Random.nextFloat() * 255).toInt(),
            (Random.nextFloat() * 255).toInt()
        )
    }

}
