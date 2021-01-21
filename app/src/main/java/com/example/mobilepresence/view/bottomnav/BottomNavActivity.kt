package com.example.mobilepresence.view.bottomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilepresence.R
import com.example.mobilepresence.databinding.ActivityBottomNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomnav.setOnNavigationItemSelectedListener(bottnavSelectedListener)
        binding.bottomnav.selectedItemId = R.id.nav_home

    }

    private val bottnavSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    binding.sample.text = "Home!"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_tc -> {
                    binding.sample.text = "Track Record!"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    binding.sample.text = "Profile"
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}