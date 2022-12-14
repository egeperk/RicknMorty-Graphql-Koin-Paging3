package com.egeperk.rickandmorty_final.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.egeperk.rickandmorty_final.R
import com.egeperk.rickandmorty_final.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding?>(
            this, R.layout.activity_main).apply {

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            menuNavBar.apply {
                setupWithNavController(navController)
                itemIconTintList = null
            }
        }
    }
}

