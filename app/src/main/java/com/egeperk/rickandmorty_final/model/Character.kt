package com.egeperk.rickandmorty_final.model

import java.io.Serializable

data class Character(val charName: String, var image: Int, var isSelected: Boolean) : Serializable {}