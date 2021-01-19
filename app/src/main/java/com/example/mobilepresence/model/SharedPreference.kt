package com.example.mobilepresence.model

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {

    //menyesuaikan dengan susunan data user
    private val APP_NAME = "MobilePresence"

    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_IS_LOGIN = "is_login"
    private val KEY_ID = "id"
    private val KEY_USERNAME = "username"
    private val KEY_EMAIL = "email"
    private val KEY_NAME = "name"
    private val KEY_DIVISION = "division"
    private val KEY_ADDRESS = "address"
    private val KEY_ROLE = "role"
    private val KEY_PHOTO_PROFILE = "photo_profile"

    private var mPref : SharedPreferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

    var isLogin : Boolean
        get() = mPref.getBoolean(KEY_IS_LOGIN, false)
        set(value) = mPref.edit().putBoolean(KEY_IS_LOGIN, value).apply()

    var accessToken : String?
        get() = mPref.getString(KEY_ACCESS_TOKEN, null)
        set(value) = mPref.edit().putString(KEY_ACCESS_TOKEN, value).apply()

    var idUser : Int?
        get() = mPref.getInt(KEY_ID, Int.MAX_VALUE)
        set(value) = mPref.edit().putInt(KEY_ID, Int.MAX_VALUE).apply()

    var userName : String?
        get() = mPref.getString(KEY_USERNAME, null)
        set(value) = mPref.edit().putString(KEY_USERNAME, value).apply()

    var emailUser : String?
        get() = mPref.getString(KEY_EMAIL, null)
        set(value) = mPref.edit().putString(KEY_EMAIL, value).apply()

    var nameUser : String?
        get() = mPref.getString(KEY_NAME,null)
        set(value) = mPref.edit().putString(KEY_NAME, value).apply()

    var divisionUser : String?
        get() = mPref.getString(KEY_DIVISION, null)
        set(value) = mPref.edit().putString(KEY_DIVISION, value).apply()

    var addressUser : String?
        get() = mPref.getString(KEY_ADDRESS, null)
        set(value) = mPref.edit().putString(KEY_ADDRESS, value).apply()

    var roleUser : String?
        get() = mPref.getString(KEY_ROLE, null)
        set(value) = mPref.edit().putString(KEY_ROLE, value).apply()

    var photo_profile : String?
        get() = mPref.getString(KEY_PHOTO_PROFILE,null)
        set(value) = mPref.edit().putString(KEY_PHOTO_PROFILE,value).apply()


    fun clear(){
        mPref.edit().clear().apply()
    }
}