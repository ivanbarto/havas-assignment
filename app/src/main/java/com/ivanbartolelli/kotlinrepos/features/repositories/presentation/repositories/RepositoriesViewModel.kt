package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ivanbartolelli.kotlinrepos.core.base_ui.BaseViewModel
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.RepositoriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(paginatedRepositoriesRepo: RepositoriesRepo) : BaseViewModel() {

    val repositories = paginatedRepositoriesRepo.getRepositories().cachedIn(viewModelScope)
}