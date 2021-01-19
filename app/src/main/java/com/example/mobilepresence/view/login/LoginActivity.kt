package com.example.mobilepresence.view.login

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.mobilepresence.databinding.ActivityLoginBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.view.bottomnav.BottomNavActivity
import com.example.mobilepresence.viewmodel.LoginViewmodel
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import timber.log.Timber
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val viewmodel: LoginViewmodel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(viewmodel.isLogin()){
            startActivity<BottomNavActivity>()
            finish()
        }

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE),111)
            else
                loginproses()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults [0] == PackageManager.PERMISSION_GRANTED)
            loginproses()
    }

    private fun loginproses() {

        var tm : TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        var data = tm.deviceId

        val loading = ProgressDialog(this)
        loading.setMessage("Log in...")

        viewmodel.getLoginResponse().observe(this, Observer {
            when(it){
                is UiState.Loading -> loading.show()
                is UiState.Success ->{
                    loading.dismiss()

                    toast("Selamat Datang " + it.data.data_user.name)

                    viewmodel.loginStatus()

                    viewmodel.savePict(it.data.data_user.picture)
                    viewmodel.saveToken(it.data.token)
                    viewmodel.saveResponse(
                        it.data.data_user.name,
                        it.data.data_user.id_user,
                        it.data.data_user.division
                    )
                    startActivity<BottomNavActivity>()
                    finish()
                }
                is UiState.Error -> {
                    loading.dismiss()
                    toast("Terjadi kesalahan")
                    Timber.tag("Error tag -> ").e("Kesalahan pada -> " + it)
                }
            }
        })

        binding.btnLogin.setOnClickListener{
            if (binding.edtUsername.text.isBlank() && binding.edtPassword.text.isBlank()){
                toast("Your data is empty!")
            }else {
                viewmodel.login(binding.edtUsername.text.toString(), binding.edtPassword.text.toString(), data.toBigInteger())
            }
        }
    }
}

