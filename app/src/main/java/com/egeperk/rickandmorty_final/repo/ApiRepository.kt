package com.egeperk.rickandmorty_final.repo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.example.rnm_mvvm.CharacterByIDQuery
import com.example.rnm_mvvm.CharactersQuery
import com.example.rnm_mvvm.EpisodesQuery

class ApiRepository(private val api: ApolloClient) {

    suspend fun queryCharList(page: Int?, query : String?): ApolloResponse<CharactersQuery.Data> {
        return api.query(CharactersQuery(Optional.Present(page), Optional.Present(query))).execute()
    }

    suspend fun queryEpisodeList(page: Int?): ApolloResponse<EpisodesQuery.Data> {
        return api.query(EpisodesQuery(Optional.Present(page))).execute()
    }

    suspend fun queryCharacter(id: String): ApolloResponse<CharacterByIDQuery.Data> {
        return api.query(CharacterByIDQuery(id)).execute()
    }
}