package com.example.mobilepresence.view.passchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilepresence.databinding.ActivityPassChangeBinding

class PassChangeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPassChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}