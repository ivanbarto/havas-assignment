package com.ivanbartolelli.assignment.features.posts.presentation.postDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class PostDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return arguments?.let { args ->
            ComposeView(requireContext()).apply {
                // MEMORY LEAK FIX
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    PostDetailScreen(
                        navController = findNavController(),
                        post = PostDetailFragmentArgs.fromBundle(args).post
                    )
                }
            }
        } ?: handleNullArguments()
    }

    private fun handleNullArguments(): View {
        findNavController().popBackStack()
        return View(requireContext())
    }
}