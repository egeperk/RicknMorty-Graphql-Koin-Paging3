package com.egeperk.rickandmorty_final.repo

import android.os.Looper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.egeperk.rickandmorty_final.util.Constants.SERVER_URL
import okhttp3.OkHttpClient

class BaseApi {

    fun getApolloClient(): ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()) {
        }
        val okHttpApiClient = OkHttpClient.Builder().build()
        return ApolloClient.Builder().serverUrl(SERVER_URL)
            .okHttpClient(okHttpApiClient).build()
    }
}