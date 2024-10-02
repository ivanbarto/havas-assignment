package com.ivanbartolelli.assignment.features.posts.data.repos

import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.PostsDatabase


class PostsRepoImpl(
    private val database: PostsDatabase
) : PostsRepo {
    override fun getPosts() = database.postsDao().getPosts()
}


