package com.egeperk.rickandmorty_final.model

import com.egeperk.rickandmorty_final.R

object CharacterProvider {

    fun provideCharacter() : ArrayList<Character> {
        val characterList = ArrayList<Character>().apply {
            add(Character("Rick", (R.drawable.ellipse1), false))
            add(Character("Morty", (R.drawable.ellipse1), false))
        }
        return characterList
    }
}