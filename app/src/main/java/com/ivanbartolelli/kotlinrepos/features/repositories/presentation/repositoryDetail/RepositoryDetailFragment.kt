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
import com.ivanbartolelli.kotlinrepos.core.base_ui.BaseFragment
import com.ivanbartolelli.kotlinrepos.databinding.RepositoryDetailsFragmentBinding
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Repository
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils.DateUtils
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils.IntentUtils
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils.ShareUtils

class RepositoryDetailFragment : BaseFragment() {

    lateinit var binding: RepositoryDetailsFragmentBinding

    lateinit var repository: Repository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return RepositoryDetailsFragmentBinding.inflate(layoutInflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            repository = RepositoryDetailFragmentArgs.fromBundle(it).repository
        }

        setupToolbar()
        setupRepositoryInfo()
        setupViewActions()
    }

    private fun setupViewActions() {
        binding.cvHttpContainer.btnCopy.setOnClickListener {
            repository.gitUrl?.let { url -> ShareUtils.copyToClipboard(url, requireContext()) }
            showCopiedToClipboardMessage()
        }

        binding.cvSshContainer.btnCopy.setOnClickListener {
            repository.sshUrl?.let { url -> ShareUtils.copyToClipboard(url, requireContext()) }
            showCopiedToClipboardMessage()
        }


        binding.btnOpenRepo.setOnClickListener {
            repository.urlAsUri()?.let { uri ->
                IntentUtils.openURLInBrowser(requireContext(), uri)
            }
        }
    }

    private fun setupRepositoryInfo() {
        binding.tvRepositoryName.text = repository.name
        binding.tvUserName.text = repository.owner?.userName
        binding.tvDescription.text = repository.description
        binding.tvWatchers.text = repository.watchersCount.toString()

        binding.cvHttpContainer.tvUrl.text = repository.gitUrl
        binding.cvSshContainer.tvUrl.text = repository.sshUrl

        binding.ivUser.ivContent.load(repository.owner?.avatarUrl) {
            placeholder(R.drawable.ic_user)
            crossfade(true)
        }

        repository.updatedAt?.let {
            binding.tvUpdateDate.text = getString(R.string.text_last_update, DateUtils.getShortDateString(it))
        }

        repository.createdAt?.let {
            binding.tvCreationDate.text = getString(R.string.text_created_at, DateUtils.getShortDateString(it))
        }
    }


    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.repository_detail_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.share -> {
                    ShareUtils.shareText(requireContext(), getString(R.string.text_share_url_message, repository.httpUrl()))
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