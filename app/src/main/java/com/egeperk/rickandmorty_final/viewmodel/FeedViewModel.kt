package com.egeperk.rickandmorty_final.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.egeperk.rickandmorty_final.adapter.pagingsource.CharacterPagingSource
import com.egeperk.rickandmorty_final.repo.ApiRepository
import com.example.rnm_mvvm.CharactersQuery
import kotlinx.coroutines.flow.Flow

class FeedViewModel(private val repository: ApiRepository) : ViewModel() {

    val search = MutableLiveData<String>()

    private var currentResult: Flow<PagingData<CharactersQuery.Result>>? = null

    fun getData(query: String): Flow<PagingData<CharactersQuery.Result>> {
        val newResult = Pager(PagingConfig(pageSize = 20)) {
            CharacterPagingSource(repository, query)
        }.flow.cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}