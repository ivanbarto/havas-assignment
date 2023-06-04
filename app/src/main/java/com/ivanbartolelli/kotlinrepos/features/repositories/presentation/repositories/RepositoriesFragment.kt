package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.ivanbartolelli.kotlinrepos.R
import com.ivanbartolelli.kotlinrepos.core.base_ui.BaseFragment
import com.ivanbartolelli.kotlinrepos.databinding.RepositoriesFragmentBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Repository
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters.RepositoriesAdapter
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters.RepositoriesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@AndroidEntryPoint
class RepositoriesFragment : BaseFragment() {

    private lateinit var binding: RepositoriesFragmentBinding

    private val repositoriesViewModel by viewModels<RepositoriesViewModel>()

    var repositoriesAdapter: RepositoriesAdapter = RepositoriesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return RepositoriesFragmentBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeRepositories()
    }

    private fun setupView() {

        binding.rvRepositories.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))

            repositoriesAdapter.also { adapter ->
                adapter.onRepositoryClick = { navigateToDetails(it) }

                adapter.addLoadStateListener { loadStates ->
                    observeRefreshLoadState(loadStates)
                }
            }

            adapter = repositoriesAdapter.withLoadStateHeaderAndFooter(
                RepositoriesLoadStateAdapter(repositoriesAdapter),
                RepositoriesLoadStateAdapter(repositoriesAdapter)
            )
        }

        binding.lySwipeRefresh.setOnRefreshListener {
            repositoriesAdapter.refresh()
        }
    }

    private fun observeRefreshLoadState(loadStates: CombinedLoadStates) {
        binding.lySwipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading

        if (loadStates.refresh is LoadState.Error) {

            when (val refreshState = (loadStates.refresh as? LoadState.Error)?.error) {
                is UnknownHostException -> {
                    customSnackbarLoader.showWarning(
                        getString(R.string.text_not_connected)
                    )
                }
                else -> {
                    customSnackbarLoader.showWarning(
                        refreshState?.message
                            ?: getString(R.string.text_unknown_error)
                    )
                }
            }
        }
    }

    private fun navigateToDetails(it: Repository) {
        val direction = RepositoriesFragmentDirections.actionRepositoriesFragmentToRepositoryDetailFragment(it)
        findNavController().navigate(direction)
    }

    private fun observeRepositories() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                repositoriesViewModel.repositories.collectLatest { pagingData ->
                    repositoriesAdapter.submitData(pagingData)
                }
            }

        }

//        repositoriesViewModel.repositoriesConnectionExists.observe(viewLifecycleOwner) { exists ->
//            if (exists) repositoriesAdapter.refresh()
//        }
    }

}