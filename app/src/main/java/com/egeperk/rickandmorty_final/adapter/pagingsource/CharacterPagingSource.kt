package com.egeperk.rickandmorty_final.adapter.pagingsource


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.egeperk.rickandmorty_final.repo.ApiRepository
import com.example.rnm_mvvm.CharactersQuery

class CharacterPagingSource(private val repository: ApiRepository, private val query: String) :
    PagingSource<Int, CharactersQuery.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersQuery.Result> {

        return try {
            val nextPageNumber = params.key ?: 1
            val response = repository.queryCharList(nextPageNumber, query)
            val nextKey = response.data?.characters?.info?.next
            val data = response.data?.characters?.results
            val characters = mapResponseToPresentationModel(data!!)
            if (!response.hasErrors()) {
                LoadResult.Page(data = characters, nextKey = nextKey, prevKey = null)
            } else {
                LoadResult.Error(Exception(response.errors?.first()?.message))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun mapResponseToPresentationModel(results: List<CharactersQuery.Result?>): List<CharactersQuery.Result> {
        val characters = mutableListOf<CharactersQuery.Result>()
        for (result in results) {
            val characterId = result?.id
            val characterImage = result?.image
            val characterName = result?.name
            val characterLocation = result?.location
            characters.add(CharactersQuery.Result(characterId,
                characterName,
                characterImage,
                characterLocation))
        }
        return characters
    }

    override fun getRefreshKey(state: PagingState<Int, CharactersQuery.Result>): Int? {
        return null
    }
}
