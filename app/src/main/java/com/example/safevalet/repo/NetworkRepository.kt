package com.example.safevalet.repo

import com.example.safevalet.api.ApiClient
import com.example.safevalet.api.ApiServices
import com.example.safevalet.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception

object NetworkRepository {
    private fun String.toFormBody() = toRequestBody("multipart/form-data".toMediaTypeOrNull())

    suspend fun userRegister(
        phone: String,
        language: String,
        name: String
    ): NetworkResults<UserRegisterModel> {
        return withContext(Dispatchers.IO){
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nameBody = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.userRegister(
                    phoneBody,
                    languageBody,
                    nameBody
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
        carMake: String,
        carModel: String,
        year: String,
        lang: String,
        jordanian: String
    ): NetworkResults<CarRegisterModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val plateNoBody = PlateNo.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val carMakeBody = carMake.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val carModelBody = carModel.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val yearBody = year.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nickNameBody = nickName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val jordanianFlag = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.carRegister(
                    userIdBody,
                    nickNameBody,
                    plateNoBody,
                    carMakeBody,
                    carModelBody,
                    yearBody,
                    languageBody,
                    jordanianFlag
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




    suspend fun carBackStatus(
        uid: String,
        lang: String
    ): NetworkResults<CarBackStatusModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.carBackStatus(
                    userIdBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


     suspend fun callBackCarModel(
        uid: String,
        lang: String
    ): NetworkResults<CallBackMyCarModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.callBackMyCar(
                    userIdBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun customerStatusZero(
        lang: String,
        uid: String,
        seen: String
    ): NetworkResults<CustomerStatusZeroModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val seenBody = seen.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getCustomerStatusZero(
                    languageBody,
                    userIdBody,
                    seenBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun outOkTrip(
        lang: String,
        uid: String,
        outOk: String
    ): NetworkResults<CustomerStatusThreeModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val outOkBody = outOk.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.outOkTrip(
                    languageBody,
                    userIdBody,
                    outOkBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }



    suspend fun customerStatusOne(
        lang: String,
        uid: String,
        ): NetworkResults<CustomerStatusOneModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val seenBody = seen?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getCustomerStatusOne(
                    languageBody,
                    userIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }



    suspend fun customerStatusTwo(
        lang: String,
        uid: String,
    ): NetworkResults<CustomerStatusTwoModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val seenBody = seen?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getCustomerStatusTwo(
                    languageBody,
                    userIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }




    suspend fun customerStatusThree(
        lang: String,
        uid: String,
        seen: String
    ): NetworkResults<CustomerStatusThreeModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val seenBody = seen.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getCustomerStatusThree(
                    languageBody,
                    userIdBody,
                    seenBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun customerStatus(
        lang: String,
        uid: String,
        seen: String


    ): NetworkResults<CustomerStatusModel> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val seenBody = seen.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//
            try {
                val results = ApiClient.retrofitService.getCustomerStatus(
                    languageBody,
                    userIdBody,
//                    seenBody

                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }



//    suspend fun setCarDefault(
//        uid: String,
//        carId: String,
//        lang: String
//
//    ): NetworkResults<SetCar> {
//        return withContext(Dispatchers.IO){
//            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val carIdBody = carId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
////            val seenBody = seen?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//            try {
//                val results = ApiClient.retrofitService.getSetCarDefault(
//                    userIdBody,
//                    carIdBody,
//                    languageBody
//
//                )
//                NetworkResults.Success(results)
//            } catch (e: Exception){
//                NetworkResults.Error(e)
//            }
//        }
//    }


    suspend fun getNotification(
        lang: String,
        uid:String

        ): NetworkResults<NotificationResponse> {
        return withContext(Dispatchers.IO){
            val langBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())



            try {
                val results = ApiClient.retrofitService.getNotification(
                    langBody,
                    uidBody

                    )
                NetworkResults.Success(results)
            } catch (e: java.lang.Exception){
                NetworkResults.Error(e)
            }
        }
    }



    suspend fun logoutUser(
        uid: String
    ): NetworkResults<Logout> {
        return withContext(Dispatchers.IO){
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getCustomerLogout(
                    userIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun checkOTPCode(
        phone: String,
        OTPCode: String,
        type: String,
        language: String,
    ) = withContext(Dispatchers.IO) {
        try {
            val results = ApiClient.retrofitService.checkOTPCode(
                phone.toFormBody(),
                OTPCode.toFormBody(),
                type.toFormBody(),
                language.toFormBody(),
            )
            NetworkResults.Success(results)
        } catch (e: Exception) {
            NetworkResults.Error(e)
        }
    }

    suspend fun resendOTPCode(
        phone: String,
        type: String,
        language: String,
    ) = withContext(Dispatchers.IO) {
        try {
            val results = ApiClient.retrofitService.resendOTPCode(
                phone.toFormBody(),
                type.toFormBody(),
                language.toFormBody(),
            )
            NetworkResults.Success(results)
        } catch (e: Exception) {
            NetworkResults.Error(e)
        }
    }


}