package com.example.safevalet.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.HelperUtils.asLiveData
import com.example.safevalet.model.BaseArrayModel
import com.example.safevalet.model.MessageModel
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.UIDData
import com.example.safevalet.repo.NetworkRepository
import kotlinx.coroutines.launch

class OTPViewModel(
    private val networkRepository: NetworkRepository,
    private val cacheUtils: HelperUtils,
    private val mContext: Context
) : ViewModel() {
    private val checkOTPCodeLiveData = MutableLiveData<NetworkResults<BaseArrayModel<UIDData>>>()
    private val resendOTPCodeLiveData = MutableLiveData<NetworkResults<MessageModel>>()

    fun checkOTPCode(phone: String, OTPCode: String, type: String) {
        viewModelScope.launch {
            val phoneBuilder = StringBuilder()
            phoneBuilder.append(phone)
            if (phone.length == 9)
                phoneBuilder.insert(0, 0)
            checkOTPCodeLiveData.value =
                networkRepository.checkOTPCode(phoneBuilder.toString(), OTPCode, type, cacheUtils.getLang(mContext))
        }
    }

    fun resendOTPCode(phone: String, type: String) {
        viewModelScope.launch {
            resendOTPCodeLiveData.value =
                networkRepository.resendOTPCode(phone, type, cacheUtils.getLang(mContext ))
        }
    }

    fun getCheckOTPCodeMessage() = checkOTPCodeLiveData.asLiveData()

    fun getResendOTPCodeMessage() = resendOTPCodeLiveData.asLiveData()
}