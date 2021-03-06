package com.example.mobilepresence.util

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import java.util.regex.Pattern

object InputCheck {

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(

        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun validationEmail(email: EditText, errormsg: String): Boolean {
        val emailText = email.text.toString()
        if (!EMAIL_ADDRESS_PATTERN.matcher(emailText).matches()) {
            email.error = errormsg
            email.requestFocus()
            return false
        } else {
            return true
        }

    }

    fun validationPassword(password: EditText, errormsg: String):Boolean{
        //val passwordText = password.text.toString()
        if (password.toString().trim().length<6){
            password.error = errormsg
            password.requestFocus()
            return false
        }else{
            return true
        }
    }

    fun validationSingkronPassword(password: EditText, konfir: EditText, errormsg: String):Boolean{
        val passwordText = password.text.toString()
        val konfirText = konfir.text.toString()
        if (!passwordText.equals (konfirText)){
            konfir.error = errormsg
            konfir.requestFocus()
            return false
        }else{
            return true
        }
    }

    fun validationInput(autocomplete: EditText, errormsg: String): Boolean {
        if (TextUtils.isEmpty(autocomplete.text.toString())) {
            autocomplete.error = errormsg
            autocomplete.requestFocus()
            return false
        } else {
            return true
        }
    }
}