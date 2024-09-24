package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.ivanbartolelli.kotlinrepos.R
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseViewHolder
import com.ivanbartolelli.kotlinrepos.databinding.RepositoryItemBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Post
import javax.inject.Inject

class PostsAdapter @Inject constructor() : PagingDataAdapter<Post, BaseViewHolder<Post>>(PostComparator) {

    lateinit var onRepositoryClick: (post: Post) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Post> {

        val itemBinding = RepositoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val holder = RepositoryViewHolder(parent, itemBinding)

        itemBinding.root.setOnClickListener {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            getItem(position)?.let { repository ->
                onRepositoryClick(repository)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Post>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class RepositoryViewHolder(private val parent: ViewGroup, private val itemBinding: RepositoryItemBinding) :
        BaseViewHolder<Post>(itemBinding.root) {

        override fun bind(item: Post): Unit = with(itemBinding) {
            tvRepositoryName.text = item.title
            tvUserName.text = parent.context.getString(R.string.text_by_user, item.author)
            tvWatchers.text = item.commentsCount.toString()

            ivUser.ivContent.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_user)
                error(R.drawable.ic_user)
            }
        }
    }


    object PostComparator : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}