package com.example.safevalet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.safevalet.databinding.ActivitySignupBinding
import com.example.safevalet.helpers.ContextWrapper
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.inVisible
import com.example.safevalet.helpers.ViewUtils.isInputEmpty
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.LoginModel
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.UserModel
import com.example.safevalet.viewmodel.UserViewModel
import retrofit2.HttpException
import java.util.*

class LoginActivity : AppCompatActivity() {      //, View.OnClickListener {

    private lateinit var binding: ActivitySignupBinding
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)



        binding.userNameCard.hide()
        userVM.getLoginResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        saveUserDataLogin(
                            result.data.data
                        )
                        val intentSignIn = Intent(this, HomeActivity::class.java)
//                        intentSignIn.putExtra("flag", "0")
                        startActivity(intentSignIn)
                        finishAffinity()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            result.data.status.msg,
                            Toast.LENGTH_SHORT
                        ).show()
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
                userVM.userLogin(
                    binding.userMobile.text.toString(),
                    language
                )
            }

            Toast.makeText(this@LoginActivity, "Logged in Successfully ", Toast.LENGTH_SHORT).show()
        }


        binding.loginActivity.setTextColor(Color.parseColor("#FFFFFF"))
//        binding.mobileField. marginTop
        binding.loginCardView.setOnClickListener {
            binding.logoutCardView.strokeColor = Color.parseColor("#2596be")
            binding.loginActivity.setTextColor(Color.parseColor("#FFFFFF"))
            binding.logoutActivity.setTextColor(Color.parseColor("#2596be"))
            binding.signUpBtn.setText(R.string.login)
            binding.userNameCard.hide()
            binding.loginCardView.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.blue))
            binding.logoutCardView.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.gray))


            userVM.getLoginResponse().observe(this) { result ->
                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.status.status == 1) {
                            saveUserDataLogin(
                                result.data.data
                            )
                            val intentSignIn = Intent(this, HomeActivity::class.java)
                            intentSignIn.putExtra("name", binding.nameUser.text.toString())
                            startActivity(intentSignIn)
                            finishAffinity()

                            Toast.makeText(
                                this@LoginActivity,
                                result.data.status.msg,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                result.data.status.msg,
                                Toast.LENGTH_SHORT
                            ).show()
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
            binding.userMobile.setText("")


            binding.signUpBtn.setOnClickListener {
                HelperUtils.hideKeyBoard(this)
                if (isInputValid()) {
                    binding.progressBarLogin.show()
                    it.inVisible()
                    userVM.userLogin(
                        binding.userMobile.text.toString(),
                        language
                    )
                }
            }
//            sharedPreferences.edit().apply {
//                bun
//            }.apply()

        }





        binding.logoutCardView.setOnClickListener {
            binding.signUpBtn.setText(R.string.sign_up)
            binding.userNameCard.show()
            binding.logoutCardView.strokeColor = Color.parseColor("#2596be")
            binding.logoutActivity.setTextColor(Color.parseColor("#FFFFFF"))
            binding.loginActivity.setTextColor(Color.parseColor("#2596be"))
            binding.loginCardView.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.gray))
            binding.logoutCardView.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue))




            userVM.getRegisterResponse().observe(this) { result ->
                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.status.status == 1) {
                            saveUserDataRegister(
                                result.data.user
                            )
                            val intentSignIn = Intent(this, HomeActivity::class.java)
                            intentSignIn.putExtra("flag", "0")
                            startActivity(intentSignIn)
                            finishAffinity()
                        } else {
                            Toast.makeText(this@LoginActivity, result.data.status.msg, Toast.LENGTH_SHORT).show()
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
            binding.userMobile.setText("")


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
                Toast.makeText(this@LoginActivity, "Successfully Registration ", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun isInputValid(): Boolean {
        var status = true

        if (binding.userMobile.isInputEmpty()) {
            status = false
            binding.userMobile.error ?: R.string.Required //getString(R.string.required)
        }

        return status
    }

    private fun saveUserDataLogin(user: LoginModel) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid", user.uid.toString())
            putString("nickname", user.car.nickName)
            putString("plateNo", user.car.palteNo)

        }.apply()
    }

    private fun saveUserDataRegister(user: UserModel) {
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

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }

}























//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//
//                if (binding.tabLayout.selectedTabPosition == 1) {
//                    Toast.makeText(
//                        this@LoginActivity,
//                        "Tab " + tab,
//                        Toast.LENGTH_LONG
//                    ).show()
//                    intent = Intent().setClass(this@LoginActivity, SignUpActivity::class.java)
//                    startActivity(intent)
////                    return HomeFragment()
//
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//
//        })




//tabLayout = binding.tabLayout
//viewPager = binding.viewPager
//
//
//tabLayout.addTab(tabLayout.newTab().setText("Login"))
//tabLayout.addTab(tabLayout.newTab().setText("Sign Up"))
//
//tabLayout.tabGravity = TabLayout.GRAVITY_FILL
//
//val adapter = TabAdapter(this, supportFragmentManager,
//    tabLayout.tabCount, view = viewPager)
//viewPager.adapter = adapter
//
//
//viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//    override fun onTabSelected(tab: TabLayout.Tab?) {
//        viewPager.currentItem = tab!!.position
//    }
//
//    override fun onTabUnselected(tab: TabLayout.Tab?) {}
//    override fun onTabReselected(tab: TabLayout.Tab?) {}
//
//})