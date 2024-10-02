package com.ivanbartolelli.assignment.features.posts.presentation.postDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ivanbartolelli.assignment.features.posts.domain.models.Post

class PostDetailFragment : Fragment() {

    lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments?.let {
            post = PostDetailFragmentArgs.fromBundle(it).post
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PostDetailScreen(
                    navController = findNavController(),
                    post = post
                )
            }
        }
    }
}