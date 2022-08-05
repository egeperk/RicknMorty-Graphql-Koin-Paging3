package com.egeperk.rickandmorty_final.model

import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.util.Constants.MORTY
import com.egeperk.rickandmorty_final.util.Constants.RICK

object CharacterProvider {

    fun provideCharacter() : ArrayList<Character> {
        val characterList = ArrayList<Character>().apply {
            add(Character(RICK, (R.drawable.ellipse1), false))
            add(Character(MORTY, (R.drawable.ellipse1), false))
        }
        return characterList
    }
}