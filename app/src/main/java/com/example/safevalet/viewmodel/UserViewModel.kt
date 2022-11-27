package com.example.safevalet.viewmodel

import android.app.Application
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

    private val CallBackMyCarLiveData = MutableLiveData<NetworkResults<CarBackStatusModel>>()
    private val CallBackCarLiveData = MutableLiveData<NetworkResults<CallBackMyCarModel>>()

    private val CustomerStatusZeroLiveData = MutableLiveData<NetworkResults<CustomerStatusZeroModel>>()
    private val CustomerStatusOneLiveData = MutableLiveData<NetworkResults<CustomerStatusOneModel>>()
    private val CustomerStatusTwoLiveData = MutableLiveData<NetworkResults<CustomerStatusTwoModel>>()
    private val CustomerStatusThreeLiveData = MutableLiveData<NetworkResults<CustomerStatusThreeModel>>()

    private val CustomerStatusLiveData = MutableLiveData<NetworkResults<CustomerStatusModel>>()
    private val SetCarDefault = MutableLiveData<NetworkResults<SetCar>>()



    private val NotificationLiveData = MutableLiveData<NetworkResults<NotificationResponse>>()

    private val logoutCustomerLiveData = MutableLiveData<NetworkResults<Logout>>()




    private val userInfoLiveData = liveData {
        Log.i("mo", "id: $user_id")
        val result = repo.showUserProfile(user_id, language)
        emit(result)
    }


    private val userHistoryLiveData = liveData {
        Log.i("mo", "id: $user_id")
        val result = repo.showUserHistory("user_id")
        emit(result)
    }

//    private val myCarsMessageLiveData = liveData {
//        Log.i("mo", "id: $user_id")
//        val result = repo.showMyCars(user_id, language)
//        emit(result)
//    }



    fun userRegister(phone: String, language: String, name: String){
        viewModelScope.launch{
            registerUserMessageLiveData.value = repo.userRegister(phone, language, name)
        }
    }

    fun userLogin(phone: String, language: String){
        viewModelScope.launch{
            loginUserMessageLiveData.value = repo.userLogin(phone, language)
        }
    }



    fun carRegister(uid: String, nickName: String, plateNo: String, carMake: String
                    , carModel: String, year: String, lang: String, jordanian: String){
        viewModelScope.launch{
            carRegisterMessageLiveData.value = repo.carRegister(uid, nickName, plateNo, carMake, carModel, year ,lang, jordanian)
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
        profileImage: File? = null
    ) {
        viewModelScope.launch {
            updateProfileLiveData.value = repo.updateUserInfo(
                language,
                userId,
                name,
                phone,
                profileImage
            )
        }
    }


    fun callBackMyCar(userId: String, language: String){
        viewModelScope.launch{
            CallBackMyCarLiveData.value = repo.callBackMyCar(userId, language)
        }
    }


    fun callBackCarModel(userId: String, language: String){
        viewModelScope.launch{
            CallBackCarLiveData.value = repo.callBackCarModel(userId, language)
        }
    }


    fun customerStatusZeroModel(language: String, userId: String, seen: String){
        viewModelScope.launch{
            CustomerStatusZeroLiveData.value = repo.customerStatusZero(language, userId, seen)
        }
    }


    fun customerStatusOneModel(language: String, userId: String){
        viewModelScope.launch{
            CustomerStatusOneLiveData.value = repo.customerStatusOne(language, userId)
        }
    }


    fun customerStatusTwoModel(language: String, userId: String){
        viewModelScope.launch{
            CustomerStatusTwoLiveData.value = repo.customerStatusTwo(language, userId)
        }
    }


    fun customerStatusThreeModel(language: String, userId: String){
        viewModelScope.launch{
            CustomerStatusThreeLiveData.value = repo.customerStatusThree(language, userId)
        }
    }


    fun customerStatusModel(language: String, userId: String){
        viewModelScope.launch{
            CustomerStatusLiveData.value = repo.customerStatus(language, userId)
        }
    }


//    fun setCar(userId: String, carId: String, language: String){
//        viewModelScope.launch{
//            SetCarDefault.value = repo.setCarDefault(userId, carId, language)
//        }
//    }



    fun logoutUser(userId: String){
        viewModelScope.launch{
            logoutCustomerLiveData.value = repo.logoutUser(userId)
        }
    }


    fun getNotificationLive(){

        viewModelScope.launch {

            NotificationLiveData.value = repo.getNotification(language, user_id)
        }
    }



    fun getRegisterResponse() = registerUserMessageLiveData
    fun getLoginResponse() = loginUserMessageLiveData
    fun getCarRegisterResponse() = carRegisterMessageLiveData
    fun getMyCarResponse() = myCarsMessageLiveData
    fun getUserInfo() = userInfoLiveData
    fun updateUserInfoResponse() = updateProfileLiveData
    fun getUserHistoryResponse() = userHistoryLiveData
    fun getBackMyCarResponse() = CallBackMyCarLiveData
    fun getCallBackCarResponse() = CallBackCarLiveData
    fun getCustomerStatusOneResponse() = CustomerStatusOneLiveData
    fun getCustomerStatusZeroResponse() = CustomerStatusZeroLiveData
    fun getCustomerStatusTwoResponse() = CustomerStatusTwoLiveData
    fun getCustomerStatusThreeResponse() = CustomerStatusThreeLiveData
    fun getCustomerStatusResponse() = CustomerStatusLiveData
    fun getSetCarResponse() = SetCarDefault

    fun getNotification() = NotificationLiveData

    fun getCustomerLogoutResponse() = logoutCustomerLiveData


}