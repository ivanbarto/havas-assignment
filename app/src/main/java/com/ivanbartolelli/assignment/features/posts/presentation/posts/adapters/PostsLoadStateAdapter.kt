package com.ivanbartolelli.assignment.features.posts.presentation.posts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.ivanbartolelli.assignment.R
import com.ivanbartolelli.assignment.core.presentation.BaseViewHolder
import com.ivanbartolelli.assignment.core.presentation.text
import com.ivanbartolelli.assignment.core.presentation.toErrorType
import com.ivanbartolelli.assignment.databinding.PostNetworkStateItemBinding

class PostsLoadStateAdapter(private val retryCallback: () -> Unit) :
    LoadStateAdapter<BaseViewHolder<LoadState>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BaseViewHolder<LoadState> {
        val itemBinding =
            PostNetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NetworkStateItemViewHolder(itemBinding, retryCallback)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LoadState>, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class NetworkStateItemViewHolder(
        private val itemBinding: PostNetworkStateItemBinding,
        private val retryCallback: () -> Unit
    ) : BaseViewHolder<LoadState>(itemBinding.root) {

        init {
            itemBinding.retryButton.setOnClickListener { retryCallback.invoke() }
        }

        override fun bind(item: LoadState) {

            with(itemBinding) {
                progressBar.isVisible = item is LoadState.Loading
                retryButton.isVisible = item is LoadState.Error
                errorMsg.apply {
                    isVisible = item is LoadState.Error
                    text = (item as? LoadState.Error)?.error?.toErrorType()?.text(context)
                        ?: context.getString(R.string.text_error_unknown)
                }
            }
        }
    }
}