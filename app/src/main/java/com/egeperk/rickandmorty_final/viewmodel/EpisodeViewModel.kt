package com.egeperk.rickandmorty_final.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.egeperk.rickandmorty_final.adapter.pagingsource.EpisodePagingSource
import com.egeperk.rickandmorty_final.repo.ApiRepository
import com.example.rnm_mvvm.EpisodesQuery
import kotlinx.coroutines.flow.Flow

class EpisodeViewModel(private val repository: ApiRepository): ViewModel() {

    private var currentResult: Flow<PagingData<EpisodesQuery.Result>>? = null

    fun getEpisodeData(): Flow<PagingData<EpisodesQuery.Result>> {
        val newResult = Pager(PagingConfig(pageSize = 20)) {
            EpisodePagingSource(repository)
        }.flow.cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}