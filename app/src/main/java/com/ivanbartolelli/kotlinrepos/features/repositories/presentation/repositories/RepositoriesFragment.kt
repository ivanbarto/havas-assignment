package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories

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
import com.ivanbartolelli.kotlinrepos.databinding.RepositoriesFragmentBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Post
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters.PostsAdapter
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters.RepositoriesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesFragment : BaseFragment() {

    private lateinit var binding: RepositoriesFragmentBinding

    private val repositoriesViewModel by viewModels<RepositoriesViewModel>()

    private var postsAdapter: PostsAdapter = PostsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return RepositoriesFragmentBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeRepositories()
    }

    private fun setupView() {
        binding.btnCleanCache.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                repositoriesViewModel.clearRepositoriesCache()
                postsAdapter.refresh()
            }
        }
        setupRecyclerView()
    }

    private fun observeRepositories() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                repositoriesViewModel.repositories.collectLatest { pagingData ->
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
                adapter.onRepositoryClick = { navigateToDetails(it) }
            }

            adapter = postsAdapter.withLoadStateHeaderAndFooter(
                RepositoriesLoadStateAdapter(postsAdapter),
                RepositoriesLoadStateAdapter(postsAdapter)
            )
        }
    }


    private fun navigateToDetails(it: Post) {
        val direction = RepositoriesFragmentDirections.actionRepositoriesFragmentToRepositoryDetailFragment(it)
        findNavController().navigate(direction)
    }


}