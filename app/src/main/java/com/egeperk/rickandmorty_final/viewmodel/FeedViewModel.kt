package com.egeperk.rickandmorty_final.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.egeperk.rickandmorty_final.adapter.ItemPagingSource
import com.egeperk.rickandmorty_final.repo.CharRepository
import com.example.rnm_mvvm.CharactersQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: CharRepository) : ViewModel() {

    private var currentResult: Flow<PagingData<CharactersQuery.Result>>? = null

    fun getData(query: String): Flow<PagingData<CharactersQuery.Result>> {
        val newResult = Pager(PagingConfig(pageSize = 20)) {
            ItemPagingSource(repository, query)
        }.flow.cachedIn(viewModelScope)
        currentResult = newResult

        return newResult
    }

}