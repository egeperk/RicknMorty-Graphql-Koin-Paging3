package com.egeperk.rickandmorty_final.di

import com.apollographql.apollo3.ApolloClient
import com.egeperk.rickandmorty_final.repo.ApiRepository
import com.egeperk.rickandmorty_final.util.Constants
import com.egeperk.rickandmorty_final.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {

    val repoModule = module {
        single { ApiRepository(get()) }
        single<ApolloClient> {
            ApolloClient.Builder().serverUrl(Constants.SERVER_URL).build()
        }
    }

    val viewModelModule = module {
        viewModel { MainViewModel(get()) }
    }
}