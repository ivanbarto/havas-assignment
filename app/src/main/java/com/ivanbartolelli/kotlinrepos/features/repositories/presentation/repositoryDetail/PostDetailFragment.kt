package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositoryDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseFragment
import com.ivanbartolelli.kotlinrepos.databinding.PostDetailFragmentBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Post

class PostDetailFragment : BaseFragment() {

    private lateinit var binding: PostDetailFragmentBinding

    lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return PostDetailFragmentBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it
                arguments?.let {
                    post = PostDetailFragmentArgs.fromBundle(it).post
                }

                binding.container.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        PostDetailScreen(
                            navController = findNavController(),
                            post = post
                        )
                    }
                }
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}