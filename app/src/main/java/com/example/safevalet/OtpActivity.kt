package com.example.safevalet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.safevalet.databinding.ActivityOtpBinding
import com.example.safevalet.fragments.ProgressDialogFragment
import com.example.safevalet.helpers.CacheUtils
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.inVisible
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.OTPInfo
import com.example.safevalet.viewmodel.OTPViewModel
import com.example.safevalet.viewmodel.UserViewModel

import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

import java.util.*

const val OTP_TYPE = "OTP_T"
const val PROGRESS_DIALOG = "progress_dialog"


class OtpActivity : AppCompatActivity() {


    private val otpViewModel by viewModels<OTPViewModel>()
    private lateinit var binding: ActivityOtpBinding
    private var countDownTimer: CountDownTimer? = null
    private lateinit var otpInfo: OTPInfo
    private val cacheUtils by inject<CacheUtils>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var otpCode = ""
        otpInfo = intent.getParcelableExtra(OTP_TYPE)!!

        otpViewModel.getCheckOTPCodeMessage().observe(this) { results ->
            binding.progressVerifyOtp.hide()
            when (results) {
                is NetworkResults.Success -> {
                    showMessage(results.data.message!!)
                    if (results.data.status == 200) {
                        checkOTPType(otpInfo, results.data.data[0].uid)
                    } else {
                        binding.progressVerifyOtp.hide()
                        binding.verifyOtpBtn.show()
                    }
                }
                is NetworkResults.Error -> {
                    showMessage(getString(R.string.error_occurred))
                    results.exception.printStackTrace()
                }
            }
        }


        otpViewModel.getResendOTPCodeMessage().observe(this) { results ->
            when (results) {
                is NetworkResults.Success -> {
                    hideDialogProgress()
                    showMessage(results.data.msg)
                    if (results.data.status == 200) {
                        binding.resendCodeTv.isEnabled = false
                        countDownTimer?.start()
                    } else {
                        binding.resendCodeTv.hide()
                        countDownTimer?.cancel()
                    }
                }
                is NetworkResults.Error -> {
                    hideDialogProgress()
                    showMessage(getString(R.string.error_occurred))
                    results.exception.printStackTrace()
                }
            }
        }

        binding.otpViewEt.setOtpCompletionListener { otpCode = it }

        binding.verifyOtpBtn.setOnClickListener {
            if (otpCode.isNotEmpty() && otpCode.length == 6) {
                it.inVisible()
                binding.progressVerifyOtp.show()
                otpViewModel.checkOTPCode(
                    otpInfo.phoneNumber,
                    otpCode,
                    otpInfo.otpType.toString(),
                )
            } else {
                showMessage(getString(R.string.correct_otp_required))
            }
        }

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCodeTv.text = getString(
                    R.string.resend_code_30,
                    (millisUntilFinished / 1000).toString(),
                )
            }

            override fun onFinish() {
                binding.resendCodeTv.setText(R.string.resend_code)
                binding.resendCodeTv.isEnabled = true
            }

        }

        if (otpInfo is OTPInfo.LoginOTP) {
            binding.resendCodeTv.setText(R.string.resend_code)
            binding.resendCodeTv.isEnabled = true
            binding.phoneNumberTv.apply {
                text = otpInfo.phoneNumber
                show()
            }
        } else {
            countDownTimer?.start()
        }

        binding.resendCodeTv.setOnClickListener {
            showDialogProgress()
            otpViewModel.resendOTPCode(otpInfo.phoneNumber, otpInfo.otpType.toString())
        }

        binding.phoneNumberTv.setOnClickListener {
            val loginOTPInfo = otpInfo as OTPInfo.LoginOTP
            val changePhoneNumberIntent = Intent(this, PhoneModificationActivity::class.java)
            changePhoneNumberIntent.putExtra(MODIFIED_PHONE_NUMBER, loginOTPInfo.phoneNumber)
            changePhoneNumberIntent.putExtra(CHANGE_PHONE_UID, loginOTPInfo.loginData.uid)
            phoneNumberModificationResult.launch(changePhoneNumberIntent)
        }
    }

    private val phoneNumberModificationResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                otpInfo.phoneNumber = result.data?.getStringExtra(MODIFIED_PHONE_NUMBER)!!
                otpViewModel.resendOTPCode(otpInfo.phoneNumber, otpInfo.otpType.toString())
            }
        }

    private fun checkOTPType(otpInfo: OTPInfo?, uid: Int) = when (otpInfo) {
        is OTPInfo.ForgetPassOTP -> {
            val intentResetPassword =
                Intent(this, ResetPasswordActivity::class.java)
            intentResetPassword.putExtra(UID_KEY, uid)
            startActivity(intentResetPassword)
        }
        is OTPInfo.LoginOTP -> {
            cacheUtils.saveUserData(otpInfo.loginData, otpInfo.userPassword, otpInfo.isRemembered)
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
            finishAffinity()
        }
        is OTPInfo.SignUpOTP -> {
            cacheUtils.saveUID(uid)
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
            finishAffinity()
        }
        else -> {
            cacheUtils.saveUID(uid)
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
            finishAffinity()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        countDownTimer = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        showMessage(getString(R.string.verification_required))
    }

    override fun attachBaseContext(newBase: Context?) {
        val language = cacheUtils.getLang()
        val configuration = Configuration()
        configuration.setLocale(Locale.forLanguageTag(language))
        val context = newBase?.createConfigurationContext(configuration)
        super.attachBaseContext(context)
    }


    private fun FragmentActivity.hideDialogProgress() {
        supportFragmentManager.findFragmentByTag(PROGRESS_DIALOG)?.let { fragment ->
            if (fragment.isAdded)
                (fragment as ProgressDialogFragment).dismiss()
        }
    }

    private fun Activity.showMessage(message: String, gravity: Int? = null) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        gravity?.let { toast.setGravity(it, 0, 0) }
        toast.show()
    }

    private fun FragmentActivity.showDialogProgress() {
        ProgressDialogFragment().show(supportFragmentManager, PROGRESS_DIALOG)
    }
}