package com.example.mobilepresence.view.passchange

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.mobilepresence.BuildConfig
import com.example.mobilepresence.databinding.ActivityPassChangeBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.util.loadImageFromUrl
import com.example.mobilepresence.view.bottomnav.BottomNavActivity
import com.example.mobilepresence.viewmodel.PassChangeViewmodel
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class PassChangeActivity : AppCompatActivity() {

    //Inisialisasi yang pada kelas XML
    private lateinit var binding: ActivityPassChangeBinding

    //Insialisasi kelas viewmodel pada layer view
    private val viewModel: PassChangeViewmodel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Menambahkan UI loading ketika pada aplikasi
        val loading = ProgressDialog(this)
        loading.setMessage("Merubah Sandi...")

        //Menjalankan fungsi respon pada kelas viewmodel untuk di observasi
        viewModel.getChangeResponse().observe(this, Observer {
            when (it) {
                is UiState.Loading -> {
                    loading.show()
                }
                is UiState.Success -> {
                    loading.dismiss()
                    startActivity<BottomNavActivity>()
                    finish()
                }
                is UiState.Error -> {
                    loading.dismiss()
                    toast("Password tidak berhasil diubah")
                }
            }
        })

        //Membuat variabel dengan value berupa metode pemanggilan data
        val intent = intent

        //mendapatkan data yang sudah di intent
        val id_user = intent.getStringExtra("id_user").toInt()
        val image_user = intent.getStringExtra("image")

        //load image url pada server
        binding.profPict.loadImageFromUrl(BuildConfig.BASE_URL+"uploads/"+image_user)

        //Button click action untuk menjalankan API pada fitur Password Change
        binding.btnConfirmChange.setOnClickListener {
            if(binding.passOne.text.isEmpty() && binding.passTwo.text.isEmpty() ||binding.passOne.text.isEmpty() || binding.passTwo.text.isEmpty()){
                toast("Password anda kosong")
            }else{
                viewModel.Passchange(id_user, binding.passOne.text.toString(), binding.passTwo.text.toString())
            }
        }
    }
}