package com.egeperk.rickandmorty_final.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.egeperk.rickandmorty_final.model.Character
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

    private var dataPage = 0
    private var currentQuery = ""
    val channel = Channel<Unit>(Channel.CONFLATED)


    private val _charactersList by lazy { MutableLiveData<State.ViewState<ApolloResponse<CharactersQuery.Data>>>() }
    val charactersList: LiveData<State.ViewState<ApolloResponse<CharactersQuery.Data>>>
        get() = _charactersList

    val characters = mutableListOf<CharactersQuery.Result>()


    init {
        queryCharList(dataPage, currentQuery)
    }

    fun queryCharList(page: Int? = 0, query: String) = viewModelScope.launch {
        _charactersList.postValue(State.ViewState.Loading())

            try {
                val response = repository.queryCharList(page, query)
                _charactersList.postValue(State.ViewState.Success(response))

            } catch (e: ApolloException) {
                _charactersList.postValue(State.ViewState.Error("Error!"))
            }



    }

    override fun onCleared() {
        _charactersList.value = null
    }
}