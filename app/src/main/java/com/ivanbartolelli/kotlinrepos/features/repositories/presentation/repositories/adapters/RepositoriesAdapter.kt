package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.ivanbartolelli.kotlinrepos.R
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseViewHolder
import com.ivanbartolelli.kotlinrepos.databinding.RepositoryItemBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Repository
import javax.inject.Inject

class RepositoriesAdapter @Inject constructor() : PagingDataAdapter<Repository, BaseViewHolder<Repository>>(RepositoryComparator) {

    lateinit var onRepositoryClick: (repository: Repository) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Repository> {

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

    override fun onBindViewHolder(holder: BaseViewHolder<Repository>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class RepositoryViewHolder(private val parent: ViewGroup, private val itemBinding: RepositoryItemBinding) :
        BaseViewHolder<Repository>(itemBinding.root) {

        override fun bind(item: Repository): Unit = with(itemBinding) {
            tvRepositoryName.text = item.name
            tvUserName.text = parent.context.getString(R.string.text_by_user, item.owner?.userName)
            tvWatchers.text = item.watchersCount.toString()

            ivUser.ivContent.load(item.owner?.avatarUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_user)
                error(R.drawable.ic_user)
            }
        }
    }


    object RepositoryComparator : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository) =
            oldItem == newItem
    }
}