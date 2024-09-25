package com.ivanbartolelli.assignment.features.repositories.presentation.posts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.ivanbartolelli.assignment.R
import com.ivanbartolelli.assignment.core.presentation.BaseViewHolder
import com.ivanbartolelli.assignment.databinding.PostNetworkStateItemBinding

class PostsLoadStateAdapter(private val postsAdapter: PostsAdapter) :
    LoadStateAdapter<BaseViewHolder<LoadState>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BaseViewHolder<LoadState> {
        val itemBinding =
            PostNetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NetworkStateItemViewHolder(itemBinding) { postsAdapter.retry() }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LoadState>, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class NetworkStateItemViewHolder(
        private val itemBinding: PostNetworkStateItemBinding,
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
                            ?: context.getString(R.string.text_generic_error)
                    )
                }
            }
        }
    }
}