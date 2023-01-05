package com.example.safevalet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safevalet.databinding.ActivityPhoneModificationBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.inVisible
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


const val MODIFIED_PHONE_NUMBER = "OLD_NUMBER"
const val CHANGE_PHONE_UID = "P_UID"

private var name: String = ""

class PhoneModificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneModificationBinding
    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = intent.getIntExtra(CHANGE_PHONE_UID, 0)
        val oldPhoneNumber = intent.getStringExtra(MODIFIED_PHONE_NUMBER)

        binding.phoneNumberEt.setText(oldPhoneNumber)

        userViewModel.updateUserInfoResponse().observe(this) { results ->
            when (results) {
                is NetworkResults.Success -> {
                    showMessage(results.data.status.msg)
                    if (results.data.status.status == 200) {
                        val changePhoneIntent = Intent()
                        changePhoneIntent.putExtra(
                            MODIFIED_PHONE_NUMBER,
                            binding.phoneNumberEt.text.toString().toJordanianPhoneFormat(),
                        )
                        name = results.data.data.userName
                        setResult(RESULT_OK, changePhoneIntent)
                        finish()
                    } else {
                        binding.changePhoneBtn.show()
                        binding.progressChangePhone.hide()
                    }
                }
                is NetworkResults.Error -> {
                    binding.changePhoneBtn.show()
                    binding.progressChangePhone.hide()
                    showMessage(getString(R.string.error_occurred))
                    results.exception.printStackTrace()
                }
            }
        }

        binding.changePhoneBtn.setOnClickListener {
            val phoneNumber = binding.phoneNumberEt.text.trim().toString()
            if (phoneNumber.isNotEmpty()) {
                it.inVisible()
                binding.progressChangePhone.show()
                if (phoneNumber == oldPhoneNumber) {
                    finish()
                } else {
                    userViewModel.updateUserProfile(
                        HelperUtils.getLang(applicationContext),
                        userId = uid.toString(),
                        name,
                        phone = phoneNumber.toJordanianPhoneFormat(),
                    )
                }
            } else {
                binding.phoneNumberEt.error = getString(R.string.required)
            }
        }
    }

    private fun String.toJordanianPhoneFormat(): String {
        return if (this.startsWith("0")) this else "0$this"
    }

    private fun Activity.showMessage(message: String, gravity: Int? = null) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        gravity?.let { toast.setGravity(it, 0, 0) }
        toast.show()
    }

}