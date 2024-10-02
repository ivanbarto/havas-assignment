package com.ivanbartolelli.assignment.features.posts.presentation.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.ivanbartolelli.assignment.R
import com.ivanbartolelli.assignment.core.presentation.BaseFragment
import com.ivanbartolelli.assignment.databinding.PostsFragmentBinding
import com.ivanbartolelli.assignment.features.posts.domain.models.Post
import com.ivanbartolelli.assignment.features.posts.presentation.posts.adapters.PostsAdapter
import com.ivanbartolelli.assignment.features.posts.presentation.posts.adapters.PostsLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : BaseFragment() {

    private lateinit var binding: PostsFragmentBinding

    private val postsViewModel by viewModels<PostsViewModel>()

    private var postsAdapter: PostsAdapter = PostsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return PostsFragmentBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        getPosts()
        observeLoadState()
    }

    private fun setupView() {
        setupRefresh()
        setupRetryButton()
        setupRecyclerView()
    }

    private fun getPosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                postsViewModel.posts.collectLatest { posts ->
                    showSuccessState(posts)
                }
            }
        }
    }

    private fun observeLoadState() {
        postsAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Error -> showErrorState(
                    (loadState.refresh as LoadState.Error).error.message
                        ?: getString(R.string.text_generic_error)
                )

                LoadState.Loading -> showLoadingState()

                is LoadState.NotLoading -> {}
            }
        }
    }

    private fun setupRetryButton() {
        binding.error.retryButton.setOnClickListener {
            postsAdapter.retry()
        }
    }

    private fun setupRefresh() {
        binding.swipeContainer.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                postsAdapter.refresh()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvRepositories.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))

            setupAdapter()

            adapter = postsAdapter.withLoadStateFooter(
                PostsLoadStateAdapter(postsAdapter)
            )
        }
    }

    private fun setupAdapter(): PostsAdapter =
        postsAdapter.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    loadStateFlow.collectLatest {
                        binding.error.container.isVisible = it.refresh is LoadState.Error
                    }
                }
            }

            onPostClick = { navigateToDetails(it) }
        }


    private fun showLoadingState() {
        binding.swipeContainer.isRefreshing = true
        binding.error.container.isVisible = false
    }

    private suspend fun showSuccessState(posts: PagingData<Post>) {
        binding.swipeContainer.isRefreshing = false
        binding.error.container.isVisible = false
        postsAdapter.submitData(posts)
    }

    private fun showErrorState(errorMessage: String) {
        binding.swipeContainer.isRefreshing = false
        binding.error.container.isVisible = true
        binding.error.errorMsg.text = errorMessage
    }


    private fun navigateToDetails(it: Post) {
        val direction =
            PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(it)
        findNavController().navigate(direction)
    }
}