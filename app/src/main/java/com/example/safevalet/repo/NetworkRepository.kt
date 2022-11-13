package com.example.safevalet.repo

import com.example.safevalet.api.ApiClient
import com.example.safevalet.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.io.File
import java.lang.Exception

object NetworkRepository {

    suspend fun userRegister(
        phone: String,
        language: String
    ): NetworkResults<UserRegisterModel> {
        return withContext(Dispatchers.IO){
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.userRegister(
                    phoneBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun userLogin(
        phone: String,
        language: String
    ): NetworkResults<UserLoginModel> {
        return withContext(Dispatchers.IO){
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.userLogin(
                    phoneBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun carRegister(
        uid: String,
        nickName: String,
        PlateNo: String,
        lang: String
    ): NetworkResults<CarRegisterModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val plateNoBody = PlateNo.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nickNameBody = nickName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.carRegister(
                    userIdBody,
                    plateNoBody,
                    nickNameBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun showMyCars(
        uid: String?,
        lang: String?
    ): NetworkResults<MyCarsModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.showMyCars(
                    userIdBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }



    suspend fun showUserProfile(
        uid: String,
        lang: String
    ): NetworkResults<UserProfileModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.viewUserProfile(
                    userIdBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun updateUserInfo(
        lang: String,
        uid: String,
        name: String,
        phone: String,
//        image: File?
    ): NetworkResults<UpdateUserInfoModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nameBody = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())

//            var imagePart: MultipartBody.Part? = null
//            image?.let {
//                imagePart = MultipartBody.Part.createFormData(
//                    "user_picture",
//                    it.name,
//                    it.asRequestBody("image/*".toMediaTypeOrNull())
//                )
//            }

            try {
                val results = ApiClient.retrofitService.updateUserInfo(
                    languageBody,
                    userIdBody,
                    nameBody,
                    phoneBody,
//                    imagePart
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun showUserHistory(
        uid: String
    ): NetworkResults<HistoryRidesModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.historyRides(
                    userIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }




    suspend fun deleteCar(
        carId: String,
        uid: String

        ): NetworkResults<RemoveCar> {
        return withContext(Dispatchers.IO) {
            val carIdBody = carId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = if (uid == "0")
                    ApiClient.retrofitService.deleteCar(
                        carIdBody,
                        userIdBody

                        )
                else ApiClient.retrofitService.deleteCar(
                    carIdBody,
                    userIdBody

                    )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun editCar(
        carId: String,
        lang: String?,
        nick_name: String?,
        car_make: String?,
        car_model: String?,
        year: String?,
        plate_no: String?

    ): NetworkResults<UpdateCar> {
        return withContext(Dispatchers.IO) {
            val carIdBody = carId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nickNameBody = nick_name?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val carMakeBody = car_make?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val carModelBody = car_model?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val yearBody = year?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val plateNoBody = plate_no?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.editCar(
                        carIdBody,
                    languageBody,
                    nickNameBody,
                    carMakeBody,
                    carModelBody,
                    yearBody,
                    plateNoBody

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

}