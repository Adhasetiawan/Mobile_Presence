package com.example.mobilepresence.view.passchange

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.mobilepresence.BuildConfig
import com.example.mobilepresence.databinding.ActivityPassChangeBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.util.Utils
import com.example.mobilepresence.util.loadImageFromUrl
import com.example.mobilepresence.view.bottomnav.BottomNavActivity
import com.example.mobilepresence.viewmodel.PassChangeViewmodel
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import timber.log.Timber

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
                    if (it.data.status == 200){
                        binding.btnConfirmChange.snackbar(
                            "Passwsord berhasil diubah!"
                        )
                    }
                }
                is UiState.Error -> {
                    if (it.throwable is HttpException){
                        if (it.throwable.code() == 404 || it.throwable.code() == 406){
                            binding.btnConfirmChange.snackbar(
                                Utils.getErrorMessage(
                                    it.throwable.response()?.errorBody()
                                )
                            )
                            loading.dismiss()
                        } else {
                            binding.btnConfirmChange.snackbar(it.throwable.message())
                        }
                    }
                    Timber.tag("Error tag -> ").e("Kesalahan pada -> " + it)
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
            viewModel.Passchange(id_user, binding.passOne.text.toString(), binding.passTwo.text.toString())
            startActivity<BottomNavActivity>()
            finish()
        }
    }
}