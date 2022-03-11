package com.egeperk.rickandmorty_final.di

import com.egeperk.rickandmorty_final.repo.CharRepository
import com.egeperk.rickandmorty_final.repo.CharacterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepo(repo: CharacterRepositoryImpl): CharRepository

}