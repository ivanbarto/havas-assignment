package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseFragment
import com.ivanbartolelli.kotlinrepos.databinding.PostsFragmentBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Post
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.posts.adapters.PostsAdapter
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.posts.adapters.PostsLoadStateAdapter
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
    }

    private fun setupView() {
        binding.btnCleanCache.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                postsViewModel.cleanPostsCache()
                postsAdapter.refresh()
            }
        }
        setupRecyclerView()
    }

    private fun getPosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                postsViewModel.repositories.collectLatest { pagingData ->
                    postsAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvRepositories.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))

            postsAdapter.also { adapter ->
                adapter.onPostClick = { navigateToDetails(it) }
            }

            adapter = postsAdapter.withLoadStateFooter(
                PostsLoadStateAdapter(postsAdapter)
            )
        }
    }

    private fun navigateToDetails(it: Post) {
        val direction =
            PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(it)
        findNavController().navigate(direction)
    }
}