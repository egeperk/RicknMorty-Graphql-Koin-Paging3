package com.egeperk.rickandmorty_final.di

import com.apollographql.apollo3.ApolloClient
import com.egeperk.rickandmorty_final.repo.CharRepository
import com.egeperk.rickandmorty_final.util.Constants
import com.egeperk.rickandmorty_final.viewmodel.FeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {

    val repoModule = module {
        single { CharRepository(get()) }
        single<ApolloClient>{
            ApolloClient.Builder().serverUrl(Constants.SERVER_URL).build()
        }
    }

    val viewModelModule = module {
        viewModel { FeedViewModel(get()) }
    }
}