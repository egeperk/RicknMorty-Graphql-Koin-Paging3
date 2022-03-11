package com.egeperk.rickandmorty_final.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.egeperk.rickandmorty_final.repo.CharRepository
import com.egeperk.rickandmorty_final.view.State
import com.example.rnm_mvvm.CharactersQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: CharRepository) : ViewModel() {

    val channel = Channel<Unit>(Channel.CONFLATED)
    var page: Int? = 0
    var currentQuery = ""
    private val _charactersList by lazy { MutableLiveData<State.ViewState<ApolloResponse<CharactersQuery.Data>>>() }
    val charactersList: LiveData<State.ViewState<ApolloResponse<CharactersQuery.Data>>>
        get() = _charactersList

    fun queryCharList() = viewModelScope.launch {
        _charactersList.postValue(State.ViewState.Loading())
        if (currentQuery == "") {
            try {
                val response = repository.queryCharList(page, currentQuery)
                _charactersList.postValue(State.ViewState.Success(response))
                page = response.data?.characters?.info?.next
            } catch (e: ApolloException) {
                _charactersList.postValue(State.ViewState.Error("Error!"))
            }
        }
        if (currentQuery == "Rick") {
            try {
                currentQuery = "Rick"
                val response = repository.queryCharList(page, currentQuery)
                _charactersList.postValue(State.ViewState.Success(response))
                page = response.data?.characters?.info?.next

            } catch (e: ApolloException) {
                _charactersList.postValue(State.ViewState.Error("Error!"))
            }
        }
        if (currentQuery == "Morty") {
            try {
                currentQuery = "Morty"
                val response = repository.queryCharList(page, currentQuery)
                _charactersList.postValue(State.ViewState.Success(response))
                page = response.data?.characters?.info?.next

            } catch (e: ApolloException) {
                _charactersList.postValue(State.ViewState.Error("Error!"))
            }
        }
    }

    override fun onCleared() {
        _charactersList.value = null
    }
}