package com.egeperk.rickandmorty_final.adapter.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.egeperk.rickandmorty_final.repo.ApiRepository
import com.example.rnm_mvvm.EpisodesQuery

class EpisodePagingSource(private val repository: ApiRepository) :
    PagingSource<Int, EpisodesQuery.Result>() {

    override fun getRefreshKey(state: PagingState<Int, EpisodesQuery.Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesQuery.Result> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = repository.queryEpisodeList(nextPageNumber)
            val nextKey = response.data?.episodes?.info?.next
            val data = response.data?.episodes?.results
            val episodes = mapResponseToPresentationModel(data!!)
            if (!response.hasErrors()) {
                LoadResult.Page(data = episodes, nextKey = nextKey, prevKey = null)
            } else {
                LoadResult.Error(Exception(response.errors?.first()?.message))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun mapResponseToPresentationModel(results: List<EpisodesQuery.Result?>): List<EpisodesQuery.Result> {
        val characters = mutableListOf<EpisodesQuery.Result>()
        for (result in results) {
            val episodeId = result?.id
            val episodeName = result?.name
            val episodeAirDate = result?.air_date
            val episode = result?.episode
            characters.add(EpisodesQuery.Result(episodeId, episodeName, episodeAirDate, episode))
        }
        return characters
    }

}