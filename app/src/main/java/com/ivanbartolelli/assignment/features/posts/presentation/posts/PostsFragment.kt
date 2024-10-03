package com.ivanbartolelli.assignment.features.posts.presentation.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.ivanbartolelli.assignment.core.presentation.ErrorType
import com.ivanbartolelli.assignment.core.presentation.ScreenState
import com.ivanbartolelli.assignment.core.presentation.text
import com.ivanbartolelli.assignment.core.presentation.toErrorType
import com.ivanbartolelli.assignment.databinding.PostsFragmentBinding
import com.ivanbartolelli.assignment.features.posts.domain.models.Post
import com.ivanbartolelli.assignment.features.posts.presentation.posts.adapters.PostsAdapter
import com.ivanbartolelli.assignment.features.posts.presentation.posts.adapters.PostsLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private var binding: PostsFragmentBinding? = null

    private val postsViewModel by viewModels<PostsViewModel>()

    private var postsAdapter: PostsAdapter = PostsAdapter(::navigateToDetails)

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
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setupView() {
        setupRefresh()
        setupRetryButton()
        setupRecyclerView()
    }


    private fun getPosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                postsViewModel.screenState.collectLatest { screenState ->
                    when (screenState) {
                        is ScreenState.Loading -> showLoadingState()

                        is ScreenState.Success ->
                            showSuccessState(screenState.data)

                        is ScreenState.Error -> showErrorState(screenState.error)
                    }
                }
            }
        }
    }

    private fun setupRetryButton() {
        binding?.error?.retryButton?.setOnClickListener {
            postsAdapter.retry()
        }
    }

    private fun setupRefresh() {
        binding?.swipeContainer?.setOnRefreshListener {
            postsAdapter.refresh()
        }
    }

    private fun setupRecyclerView() {
        binding?.rvRepositories?.apply {

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
            addLoadStateListener { state ->
                when (state.refresh) {
                    is LoadState.Error -> showErrorState(
                        (state.refresh as LoadState.Error).error.toErrorType()
                    )

                    LoadState.Loading -> showLoadingState()

                    is LoadState.NotLoading -> Unit
                }
            }
        }


    private fun showLoadingState() {
        binding?.let { binding ->
            binding.swipeContainer.isRefreshing = true
            binding.error.container.isVisible = false
        }
    }

    private suspend fun showSuccessState(posts: PagingData<Post>) {
        binding?.let { binding ->
            binding.swipeContainer.isRefreshing = false
            binding.error.container.isVisible = false
        }
        postsAdapter.submitData(posts)
    }

    private fun showErrorState(error: ErrorType) {
        binding?.let { binding ->
            binding.swipeContainer.isRefreshing = false
            binding.error.container.isVisible = true
            binding.error.errorMsg.text = error.text(requireContext())
        }
    }


    private fun navigateToDetails(it: Post) {
        val direction =
            PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(it)
        findNavController().navigate(direction)
    }
}