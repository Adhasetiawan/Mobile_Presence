package com.example.mobilepresence.view.login

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.mobilepresence.databinding.ActivityLoginBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.util.InputCheck
import com.example.mobilepresence.util.Utils
import com.example.mobilepresence.view.bottomnav.BottomNavActivity
import com.example.mobilepresence.viewmodel.LoginViewmodel
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewmodel: LoginViewmodel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewmodel.isLogin()) {
            startActivity<BottomNavActivity>()
            finish()
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                111
            )
        else
            loginproses()

        // send email akan menyesuikan diakahir
        binding.intentForgetPass.setOnClickListener {
            val sendEmailto :String = "Adhawiyana@gmail.com".toString().trim()
            val subjectEmail :String = "Admin call".toString().trim()
            val messageEmail :String = ("Good night\n" +
                                        "This Massage only for testing purpose\n"+
                                        "So let see how it's gonna be!").toString().trim()

            contactAdmin(sendEmailto, subjectEmail, messageEmail)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            loginproses()
    }

    private fun loginproses() {

        val loading = ProgressDialog(this)
        loading.setMessage("Log in...")


        var tm: TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        var data = tm.deviceId


        viewmodel.getLoginResponse().observe(this, Observer {
            when (it) {
                is UiState.Loading -> loading.show()
                is UiState.Success -> {
                    loading.dismiss()

                    toast("Welcome " + it.data.data_user.name)

                    viewmodel.loginStatus()

                    viewmodel.savePict(it.data.data_user.picture)
                    viewmodel.saveToken(it.data.token)
                    viewmodel.saveResponse(
                        it.data.data_user.name,
                        it.data.data_user.id_user,
                        it.data.data_user.division,
                        it.data.data_user.role
                    )
                    startActivity<BottomNavActivity>()
                    finish()
                }
                is UiState.Error -> {
                    if (it.throwable is HttpException) {
                        if (it.throwable.code() == 404) {
                            binding.btnLogin.snackbar(
                                Utils.getErrorMessage(
                                    it.throwable.response()?.errorBody()
                                )
                            )
                            loading.dismiss()
                        } else {
                            binding.btnLogin.snackbar(it.throwable.message())
                        }
                    }
                    Timber.tag("Error tag -> ").e("Kesalahan pada -> " + it)
                }
            }
        })

        binding.btnLogin.setOnClickListener {
            if (binding.edtUsername.text.isBlank() && binding.edtPassword.text.isBlank()) {
                toast("Your data is empty!")
            } else {
                inputCheck()
                viewmodel.login(
                    binding.edtUsername.text.toString(),
                    binding.edtPassword.text.toString(),
                    data.toBigInteger()
                )
            }
        }
    }

    private fun inputCheck(): Boolean {
        val emailcheck = InputCheck.validationEmail(binding.edtUsername, "Incorrect Email")
        val passcheck = InputCheck.validationPassword(binding.edtPassword, "Incorrect Password")
        return emailcheck && passcheck
    }

    private fun contactAdmin(sendTo : String, subjectEmail : String, message: String){
        val mIntent = Intent(Intent.ACTION_SEND)

        mIntent.data = Uri.parse("mailTo : ")
        mIntent.type = "text/plain"

        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(sendTo))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subjectEmail)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        startActivity(Intent.createChooser(mIntent, "Choose Email client..."))
    }
}