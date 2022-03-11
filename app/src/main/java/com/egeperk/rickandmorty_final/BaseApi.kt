package com.egeperk.rickandmorty_final

import android.os.Looper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

class BaseApi {

    fun getApolloClient(): ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()) {

        }

        val okHttpApiClient = OkHttpClient.Builder().build()
        return ApolloClient.Builder().serverUrl("https://rickandmortyapi.com/graphql").okHttpClient(okHttpApiClient).build()

    }

}