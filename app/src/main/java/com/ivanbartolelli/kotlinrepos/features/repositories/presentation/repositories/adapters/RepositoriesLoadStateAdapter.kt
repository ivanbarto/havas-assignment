package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.ivanbartolelli.kotlinrepos.R
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseViewHolder
import com.ivanbartolelli.kotlinrepos.databinding.RepositoryNetworkStateItemBinding

class RepositoriesLoadStateAdapter(private val repositoriesAdapter: RepositoriesAdapter) : LoadStateAdapter<BaseViewHolder<LoadState>>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BaseViewHolder<LoadState> {
        val itemBinding = RepositoryNetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NetworkStateItemViewHolder(itemBinding, parent.context) { repositoriesAdapter.retry() }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LoadState>, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class NetworkStateItemViewHolder(
        private val itemBinding: RepositoryNetworkStateItemBinding,
        context: Context,
        private val retryCallback: () -> Unit
    ) : BaseViewHolder<LoadState>(itemBinding.root) {

        init {
            itemBinding.retryButton.setOnClickListener { retryCallback() }
        }

        override fun bind(item: LoadState) {

            with(itemBinding) {
                progressBar.isVisible = item is LoadState.Loading
                retryButton.isVisible = item is LoadState.Error
                errorMsg.apply {
                    isVisible = item is LoadState.Error
                    text = context.getString(
                        R.string.text_error,
                        (item as? LoadState.Error)?.error?.message
                            ?: R.string.text_generic_error
                    )
                }
            }
        }
    }
}