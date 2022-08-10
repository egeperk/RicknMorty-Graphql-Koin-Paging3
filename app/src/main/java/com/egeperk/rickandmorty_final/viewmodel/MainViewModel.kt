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
import com.egeperk.rickandmorty_final.util.Constants.EMPTY
import com.example.rnm_mvvm.CharactersQuery
import com.example.rnm_mvvm.EpisodesQuery
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ApiRepository): ViewModel() {

    val search = MutableStateFlow(EMPTY)
    val isSelected = MutableStateFlow(false)

    private val _charResult = MutableStateFlow<PagingData<CharactersQuery.Result>>(PagingData.empty())
    val charResult = _charResult.asStateFlow()

    private val _episodeResult = MutableStateFlow<PagingData<EpisodesQuery.Result>>(PagingData.empty())
    val episodeResult = _episodeResult.asStateFlow()


    private fun getEpisodeData(): StateFlow<PagingData<EpisodesQuery.Result>> {
        viewModelScope.launch {
            val newResult = Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
                EpisodePagingSource(repository)
            }.flow.cachedIn(viewModelScope).stateIn(viewModelScope)
            _episodeResult.value = newResult.value
        }
        return _episodeResult
    }

    fun getData(query: String): StateFlow<PagingData<CharactersQuery.Result>>{
        viewModelScope.launch {
            val newResult = Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
                CharacterPagingSource(repository, query)
            }.flow.cachedIn(viewModelScope).stateIn(viewModelScope)
            _charResult.value = newResult.value
        }
        return charResult
    }

    init {
        getData(EMPTY)
        getEpisodeData()
    }
}