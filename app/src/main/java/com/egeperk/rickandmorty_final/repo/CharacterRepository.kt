package com.egeperk.rickandmorty_final.repo

import com.apollographql.apollo3.api.ApolloResponse
import com.example.rnm_mvvm.CharactersQuery

interface CharRepository {

    suspend fun queryCharList(page: Int?, query : String?): ApolloResponse<CharactersQuery.Data>

    suspend fun queryRickList(page: Int?, query : String?): ApolloResponse<CharactersQuery.Data>

    suspend fun queryMortyList(page: Int?, query : String?): ApolloResponse<CharactersQuery.Data>

}