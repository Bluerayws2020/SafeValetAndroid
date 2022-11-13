package com.example.safevalet.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.model.*
import com.example.safevalet.repo.NetworkRepository
import kotlinx.coroutines.launch
import java.io.File


class UserViewModel(application: Application): AndroidViewModel(application) {

    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)
    private val repo = NetworkRepository
    private val language = "ar"
//    private val uid = HelperUtils.getUID(application.applicationContext)\
    private val sharedpreferences: SharedPreferences =
        application.getSharedPreferences(HelperUtils.SHARED_PREF, AppCompatActivity.MODE_PRIVATE)

    private val user_id = sharedpreferences.getString("uid", null).toString()


    private val registerUserMessageLiveData = MutableLiveData<NetworkResults<UserRegisterModel>>()
    private val loginUserMessageLiveData = MutableLiveData<NetworkResults<UserLoginModel>>()

    private val carRegisterMessageLiveData = MutableLiveData<NetworkResults<CarRegisterModel>>()

    private val myCarsMessageLiveData =  MutableLiveData<NetworkResults<MyCarsModel>>()
    private val updateProfileLiveData = MutableLiveData<NetworkResults<UpdateUserInfoModel>>()
    private val deleteCarLiveData = MutableLiveData<NetworkResults<RemoveCar>>()
    private val updateCarLiveData = MutableLiveData<NetworkResults<UpdateCar>>()



    private val userInfoLiveData = liveData {
        Log.i("mo", "id: $user_id")
        val result = repo.showUserProfile(user_id, language)
        emit(result)
    }


    private val userHistoryLiveData = liveData {
        Log.i("mo", "id: $user_id")
        val result = repo.showUserHistory(user_id)
        emit(result)
    }

//    private val myCarsMessageLiveData = liveData {
//        Log.i("mo", "id: $user_id")
//        val result = repo.showMyCars(user_id, language)
//        emit(result)
//    }



    fun userRegister(phone: String, language: String){
        viewModelScope.launch{
            registerUserMessageLiveData.value = repo.userRegister(phone, language)
        }
    }

    fun userLogin(phone: String, language: String){
        viewModelScope.launch{
            loginUserMessageLiveData.value = repo.userLogin(phone, language)
        }
    }



    fun carRegister(uid: String, nickName: String, plateNo: String, lang: String){
        viewModelScope.launch{
            carRegisterMessageLiveData.value = repo.carRegister(uid, nickName, plateNo, lang)
        }
    }


    fun showMyCars(){
        viewModelScope.launch{
            val result = NetworkRepository.showMyCars(user_id, language)
            myCarsMessageLiveData.value = result
        }
    }



    fun removeCar(carId: String) {
        viewModelScope.launch {
            deleteCarLiveData.value =
                NetworkRepository.deleteCar(carId, user_id)


            Log.d("UID@",user_id.toString())
        }
    }


    fun updateCar(carId: String, lang:String, nickName: String, carMake:String, carModel:String, year:String ,plateNo: String) {
        viewModelScope.launch {
            updateCarLiveData.value =
                NetworkRepository.editCar(carId, lang, nickName, carMake
                , carModel, year, plateNo)
        }
    }

    fun updateUserProfile(
        language: String,
        userId: String,
        name: String,
        phone: String,
//        profileImage: File? = null
    ) {
        viewModelScope.launch {
            updateProfileLiveData.value = repo.updateUserInfo(
                language,
                userId,
                name,
                phone,
//                profileImage
            )
        }
    }


    fun getRegisterResponse() = registerUserMessageLiveData
    fun getLoginResponse() = loginUserMessageLiveData
    fun getCarRegisterResponse() = carRegisterMessageLiveData
    fun getMyCarResponse() = myCarsMessageLiveData
    fun getUserInfo() = userInfoLiveData
    fun updateUserInfoResponse() = updateProfileLiveData
    fun getUserHistoryResponse() = userHistoryLiveData

}