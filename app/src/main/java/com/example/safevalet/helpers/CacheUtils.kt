package com.example.safevalet.helpers

import android.content.SharedPreferences
import com.example.safevalet.model.LoginModel

const val ENGLISH_USER_LANGUAGE = "1"
const val ARABIC_USER_LANGUAGE = "2"
const val ARABIC_LANGUAGE = "ar"
const val ENGLISH_LANGUAGE = "en"
const val LANGUAGE_KEY = "lang"
const val SAVED_UID = "uid"
const val BASE_URL = "https://valet-jo.com/"


class CacheUtils(private val sharedPreferences: SharedPreferences) {

    fun getBaseUrl(): String {
        return BASE_URL
    }

    fun getLang(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, ARABIC_LANGUAGE)!!
    }

    fun getUserLanguage(): String {
        return if (getLang() == ARABIC_LANGUAGE)
            ARABIC_USER_LANGUAGE
        else
            ENGLISH_USER_LANGUAGE
    }

    fun changeLanguage() {
        val currentLanguage = getLang()
        val newLanguage = if (currentLanguage == ARABIC_LANGUAGE)
            ENGLISH_LANGUAGE
        else
            ARABIC_LANGUAGE
        sharedPreferences.edit().apply {
            putString(LANGUAGE_KEY, newLanguage)
        }.apply()
    }

    fun getUID(): Int {
        return sharedPreferences.getInt(SAVED_UID, 0)
    }

    fun saveUID(userId: Int) {
        sharedPreferences.edit().apply {
            putInt(SAVED_UID, userId)
        }.apply()
    }

    fun changePassword(password: String) {
        sharedPreferences.edit()
            .putString("pass", password)
            .apply()
    }

    fun changeEmail(email: String) {
        sharedPreferences.edit()
            .putString("email", email)
            .apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getInt(SAVED_UID, 0) != 0
    }

    fun isGuest(): Boolean {
        return sharedPreferences.getInt(SAVED_UID, 0) == 0
    }

    fun checkUserRememberInfoListener(userInfoCallback: (String?, String?) -> Unit) {
        val isRemembered = sharedPreferences.getBoolean("is_remember", false)
        if (isRemembered) {
            val userEmail = sharedPreferences.getString("email", null)
            val userPass = sharedPreferences.getString("pass", null)
            userInfoCallback.invoke(userEmail, userPass)
        }
    }

    fun logout() {
        val isRemembered = sharedPreferences.getBoolean("is_remember", false)
        if (isRemembered)
            sharedPreferences.edit().remove(SAVED_UID).apply()
        else {
            val language = getLang()
            sharedPreferences.edit().apply {
                clear()
                putString(LANGUAGE_KEY, language)
            }.apply()
        }
    }

    //TODO ensure phone number is not modified from otp flow after login @
    fun saveUserData(
        userLoginData: LoginModel,
        userPass: String? = null,
        isRemembered: Boolean = false
    ) {
        sharedPreferences.edit().apply {
            putInt(SAVED_UID, userLoginData.uid.toInt())
//            putString("user_name", userLoginData.)
//            putString("role", userLoginData.role)
//            putString("email", userLoginData.email)
            putString("phone", userLoginData.phone)
//            putString("pass", userPass)
            putBoolean("is_remember", isRemembered)
        }.apply()
    }
}