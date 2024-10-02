package com.ivanbartolelli.assignment.features.posts.domain.interactor

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.mediators.PostsRemoteMediator
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.utils.PostsConstants
import com.ivanbartolelli.assignment.features.posts.data.repos.PostsRepo
import com.ivanbartolelli.assignment.features.posts.domain.models.toPost
import kotlinx.coroutines.flow.map

class PostsInteractorImpl(
    private val postsRemoteMediator: PostsRemoteMediator,
    private val postsRepo: PostsRepo
) : PostsInteractor {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts() = Pager(
        config = PagingConfig(
            pageSize = PostsConstants.ITEMS_PER_PAGE,
            prefetchDistance = PostsConstants.ITEMS_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {
            postsRepo.getPosts()
        },
        remoteMediator = postsRemoteMediator
    ).flow.map { it.map { data -> data.toPost() } }
}