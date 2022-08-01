package com.egeperk.rickandmorty_final.repo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.example.rnm_mvvm.CharactersQuery

class CharRepository(private val api: ApolloClient) {

    suspend fun queryCharList(page: Int?, query : String?): ApolloResponse<CharactersQuery.Data> {
        return api.query(CharactersQuery(Optional.Present(page), Optional.Present(query))).execute()
    }
}