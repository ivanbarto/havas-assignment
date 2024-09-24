package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositoryDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.ivanbartolelli.kotlinrepos.R
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseFragment
import com.ivanbartolelli.kotlinrepos.databinding.RepositoryDetailsFragmentBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Post
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils.DateUtils
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils.IntentUtils
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils.ShareUtils

class RepositoryDetailFragment : BaseFragment() {

    lateinit var binding: RepositoryDetailsFragmentBinding

    lateinit var post: Post

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return RepositoryDetailsFragmentBinding.inflate(layoutInflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            post = RepositoryDetailFragmentArgs.fromBundle(it).post
        }

        setupToolbar()
        setupRepositoryInfo()
        setupViewActions()
    }

    private fun setupViewActions() {
//      setupViewActions  binding.cvHttpContainer.btnCopy.setOnClickListener {
//            post.gitUrl?.let { url -> ShareUtils.copyToClipboard(url, requireContext()) }
//            showCopiedToClipboardMessage()
//        }
//
//        binding.cvSshContainer.btnCopy.setOnClickListener {
//            post.sshUrl?.let { url -> ShareUtils.copyToClipboard(url, requireContext()) }
//            showCopiedToClipboardMessage()
//        }


        binding.btnOpenRepo.setOnClickListener {
            post.urlAsUri()?.let { uri ->
                IntentUtils.openURLInBrowser(requireContext(), uri)
            }
        }
    }

    private fun setupRepositoryInfo() {
        binding.tvRepositoryName.text = post.title
        binding.tvUserName.text = post.author
        binding.tvDescription.text = post.body
        binding.tvWatchers.text = post.commentsCount.toString()

//        binding.cvHttpContainer.tvUrl.text = post.gitUrl
//        binding.cvSshContainer.tvUrl.text = post.sshUrl

//        binding.ivUser.ivContent.load(post.owner?.avatarUrl) {
//            placeholder(R.drawable.ic_user)
//            crossfade(true)
//        }
//
//        post.updatedAt?.let {
//            binding.tvUpdateDate.text = getString(R.string.text_last_update, DateUtils.getShortDateString(it))
//        }
//
//        post.createdAt?.let {
//            binding.tvCreationDate.text = getString(R.string.text_created_at, DateUtils.getShortDateString(it))
//        }
    }


    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.repository_detail_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.share -> {
                    ShareUtils.shareText(requireContext(), getString(R.string.text_share_url_message, post.httpUrl()))
                }
            }

            return@setOnMenuItemClickListener true
        }


        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
    }

    private fun showCopiedToClipboardMessage() {
        customSnackbarLoader.showInfo(getString(R.string.text_copied))
    }
}