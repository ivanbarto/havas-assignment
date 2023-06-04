package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseViewModel
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.RepositoriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(paginatedRepositoriesRepo: RepositoriesRepo, @ApplicationContext context: Context) :
    BaseViewModel() {

    val repositories = paginatedRepositoriesRepo.getRepositories().cachedIn(viewModelScope)
}