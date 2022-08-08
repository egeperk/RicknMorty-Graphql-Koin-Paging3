package com.egeperk.rickandmorty_final.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.egeperk.rickandmorty_final.adapter.pagingsource.CharacterPagingSource
import com.egeperk.rickandmorty_final.adapter.pagingsource.EpisodePagingSource
import com.egeperk.rickandmorty_final.repo.ApiRepository
import com.egeperk.rickandmorty_final.util.Constants
import com.example.rnm_mvvm.CharactersQuery
import com.example.rnm_mvvm.EpisodesQuery
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: ApiRepository): ViewModel() {

    val search = MutableLiveData<String>()

    private var currentEpisodeResult: Flow<PagingData<EpisodesQuery.Result>>? = null
    private var currentCharacterResult: Flow<PagingData<CharactersQuery.Result>>? = null


    fun getEpisodeData(): Flow<PagingData<EpisodesQuery.Result>> {
        val newResult = Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
            EpisodePagingSource(repository)
        }.flow.cachedIn(viewModelScope)
        currentEpisodeResult = newResult
        return newResult
    }

    fun getData(query: String): Flow<PagingData<CharactersQuery.Result>> {
        val newResult = Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
            CharacterPagingSource(repository, query)
        }.flow.cachedIn(viewModelScope)
        currentCharacterResult = newResult
        return newResult
    }

}