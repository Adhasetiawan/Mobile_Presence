package com.example.mobilepresence.view.passchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilepresence.databinding.ActivityPassChangeBinding
import com.example.mobilepresence.view.bottomnav.BottomNavActivity
import org.jetbrains.anko.design.snackbar

class PassChangeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPassChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirmChange.setOnClickListener {
            snackbar(binding.btnConfirmChange, "This page is working fine!")
            startActivity(Intent(this, BottomNavActivity::class.java))
            finish()
        }
    }
}