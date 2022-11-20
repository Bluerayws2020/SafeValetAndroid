package com.example.safevalet

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.safevalet.databinding.ActivitySignupBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.inVisible
import com.example.safevalet.helpers.ViewUtils.isInputEmpty
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.LoginModel
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.UserModel
import com.example.safevalet.model.UserRegisterModel
import com.example.safevalet.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayout
import retrofit2.HttpException

class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        userVM.getRegisterResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        saveUserData(
                            result.data.user
                        )
                        val intentSignIn = Intent(this, HomeActivity::class.java)
                        intentSignIn.putExtra("flag", "0")
                        startActivity(intentSignIn)
                        finishAffinity()
                    } else {
                        Toast.makeText(this@SignUpActivity, result.data.status.msg, Toast.LENGTH_SHORT).show()
//                        Log.i("TAG", "onCreate: " + result.data.user.car)
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                    if (result.exception is HttpException)
                        showMessage(result.exception.message())
                    else
                        showMessage("No Internet connection")
                }
            }
        }

        binding.signUpBtn.setOnClickListener {
            HelperUtils.hideKeyBoard(this)
            if (isInputValid()) {
                binding.progressBarLogin.show()
                it.inVisible()
                userVM.userRegister(
                    binding.userMobile.text.toString(),
                    language,
                    binding.nameUser.text.toString()
                )
            }
        }




//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//
//           if (binding.tabLayout.selectedTabPosition == 1) {
//
////                    Toast.makeText(
////                        this@SignUpActivity,
////                        "Tab " + binding.tabLayout.selectedTabPosition,
////                        Toast.LENGTH_LONG
////                    ).show()
//                    intent = Intent().setClass(this@SignUpActivity, LoginActivity::class.java)
//                    startActivity(intent)
////                    return HomeFragment()
//                    tab?.select()
//
//
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//
//        })


        binding.loginCardView.strokeColor = Color.parseColor("#2596be")
        binding.logoutCardView.setCardBackgroundColor(Color.parseColor("#2596be"))
        binding.logoutActivity.setTextColor(Color.parseColor("#FFFFFF"))

        binding.loginActivity.setOnClickListener {
            intent = Intent().setClass(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)

        }

    }



    private fun isInputValid(): Boolean {
        var status = true

        if (binding.userMobile.isInputEmpty()) {
            status = false
            binding.userMobile.error ?: "Required" //getString(R.string.required)
        }

        return status
    }

    private fun saveUserData(user: UserModel) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid", user.uid.toString())
            putString("user_type", user.type)

        }.apply()
    }

    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun hideProgress() {
        binding.signUpBtn.show()
        binding.progressBarLogin.hide()
    }

}