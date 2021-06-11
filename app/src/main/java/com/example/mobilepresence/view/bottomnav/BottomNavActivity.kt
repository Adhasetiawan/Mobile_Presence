package com.example.mobilepresence.view.bottomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobilepresence.R
import com.example.mobilepresence.databinding.ActivityBottomNavBinding
import com.example.mobilepresence.view.absence.AbsenceFragment
import com.example.mobilepresence.view.home.HomeFragment
import com.example.mobilepresence.view.profile.ProfileFragment
import com.example.mobilepresence.view.trackrecord.TrackRecordFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavBinding

    private val homeFrag = HomeFragment()
    private val absenceFrag = AbsenceFragment()
    private val trackRecFrag = TrackRecordFragment()
    private val profileFrag = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomnav.setOnNavigationItemSelectedListener(bottnavSelectedListener)

        currentpage(homeFrag)
    }

    private fun currentpage(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_placer, fragment)
            commit()
        }
    }

    private val bottnavSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    currentpage(homeFrag)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_absence ->{
                    currentpage(absenceFrag)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_tc -> {
                    currentpage(trackRecFrag)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    currentpage(profileFrag)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}