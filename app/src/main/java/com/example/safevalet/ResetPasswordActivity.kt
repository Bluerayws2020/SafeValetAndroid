package com.example.safevalet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.safevalet.databinding.ActivityResetPasswordBinding
import com.example.safevalet.helpers.CacheUtils
import com.example.safevalet.helpers.ViewUtils.isInputEmpty
import com.example.safevalet.viewmodel.UserViewModel
import org.koin.android.ext.android.inject
import java.util.*


const val FORGET_PASS_TYPE = 2

//const val RESET_PASS_TYPE = 3
const val UID_KEY = "UID"

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private val userVM by viewModels<UserViewModel>()
    private val cacheUtils by inject<CacheUtils>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.extras?.getInt(UID_KEY)

//        userVM.getResetPasswordMessage().observe(this) { result ->
//            when (result) {
//                is NetworkResults.Success -> {
//                    showMessage(result.data.message)
//                    if (result.data.status == 200) {
//                        val intentLogin = Intent(this, LoginActivity::class.java)
//                        startActivity(intentLogin)
//                        finishAffinity()
//                    } else {
//                        binding.progressResetPass.hide()
//                        binding.submitNewPasswordBtn.show()
//                    }
//                }
//                is NetworkResults.Error -> {
//                    showMessage(getString(R.string.error_occurred))
//                    result.exception.printStackTrace()
//                }
//            }
//        }

//        binding.submitNewPasswordBtn.setOnClickListener {
//            if (isInputValid()) {
//                it.hide()
//                binding.progressResetPass.show()
//                userVM.resetPassword(
//                    userId = userId.toString(),
//                    currentPass = null,
//                    newPass = binding.newPasswordEt.text.toString(),
//                    type = FORGET_PASS_TYPE.toString()
//                )
//            }
//        }
    }

        private fun isInputValid(): Boolean {
            var state = true

            /*if (binding.oldPasswordEt.isInputEmpty()) {
            state = false
            binding.oldPasswordEt.error = getString(R.string.required)
        }*/
            if (binding.newPasswordEt.isInputEmpty()) {
                state = false
                binding.newPasswordEt.error = getString(R.string.required)
            }
            if (binding.rePasswordEt.isInputEmpty()) {
                state = false
                binding.rePasswordEt.error = getString(R.string.required)
            }
            if (binding.newPasswordEt.text.toString().trim() !=
                binding.rePasswordEt.text.toString().trim()
            ) {
                state = false
                binding.newPasswordEt.error = getString(R.string.pass_not_match)
                binding.rePasswordEt.error = getString(R.string.pass_not_match)
            }

            return state
        }


        private fun Activity.showMessage(message: String, gravity: Int? = null) {
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            gravity?.let { toast.setGravity(it, 0, 0) }
            toast.show()
        }

        override fun attachBaseContext(newBase: Context?) {
            val language = cacheUtils.getLang()
            val configuration = Configuration()
            configuration.setLocale(Locale.forLanguageTag(language))
            val context = newBase?.createConfigurationContext(configuration)
            super.attachBaseContext(context)
        }


}