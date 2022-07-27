package com.egeperk.rickandmorty_final.repo

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.egeperk.rickandmorty_final.BaseApi
import com.example.rnm_mvvm.CharactersQuery
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val service: BaseApi) : CharRepository {

    override suspend fun queryCharList(
        page: Int?,
        query: String?
    ): ApolloResponse<CharactersQuery.Data> {
      return service.apolloClient.query(CharactersQuery(Optional.Present(page), Optional.Present(query))).execute()
          }
}