package com.ivanbartolelli.assignment.features.posts.presentation.posts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.ivanbartolelli.assignment.R
import com.ivanbartolelli.assignment.core.presentation.BaseViewHolder
import com.ivanbartolelli.assignment.databinding.PostItemBinding
import com.ivanbartolelli.assignment.features.posts.domain.models.Post

class PostsAdapter(val onPostClick: (post: Post) -> Unit) :
    PagingDataAdapter<Post, BaseViewHolder<Post>>(PostComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Post> {

        val itemBinding =
            PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val holder = RepositoryViewHolder(itemBinding)

        itemBinding.root.setOnClickListener {
            val position =
                holder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            getItem(position)?.let { repository ->
                onPostClick(repository)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Post>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class RepositoryViewHolder(private val itemBinding: PostItemBinding) :
        BaseViewHolder<Post>(itemBinding.root) {

        override fun bind(item: Post): Unit = with(itemBinding) {
            tvTitle.text = item.title
            ivPreview.load(item.imageUrl) {
                crossfade(true)
            }

            cardUpVotes.ivDataIcon.setImageResource(R.drawable.ic_arrow_updown)
            cardUpVotes.tvData.text = item.ups.toString()

            cardComments.ivDataIcon.setImageResource(R.drawable.ic_comment)
            cardComments.tvData.text = item.commentsCount.toString()
        }
    }


    object PostComparator : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id
    }
}