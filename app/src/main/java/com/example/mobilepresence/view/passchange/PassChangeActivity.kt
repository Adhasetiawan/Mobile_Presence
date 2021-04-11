package com.example.mobilepresence.view.passchange

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.mobilepresence.databinding.ActivityPassChangeBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.util.Utils
import com.example.mobilepresence.view.bottomnav.BottomNavActivity
import com.example.mobilepresence.viewmodel.PassChangeViewmodel
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import timber.log.Timber

class PassChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPassChangeBinding

    private val viewModel: PassChangeViewmodel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loading = ProgressDialog(this)
        loading.setMessage("Merubah Sandi...")

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

        val intent = intent
        val id_user = intent.getStringExtra("id_user").toInt()

        binding.subTittlePass.text = id_user.toString()

        binding.btnConfirmChange.setOnClickListener {
            viewModel.Passchange(id_user, binding.passOne.text.toString(), binding.passTwo.text.toString())
            toast(id_user.toString() + " it works")
            startActivity<BottomNavActivity>()
            finish()
        }
    }
}