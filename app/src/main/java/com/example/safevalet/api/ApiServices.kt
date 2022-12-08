package com.example.safevalet.api

import android.media.tv.TvContract.Channels.Logo
import android.util.Log
import com.example.safevalet.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {


    @Multipart
    @POST("SignUp")
    suspend fun userRegister(
        @Part("phone") phone: RequestBody,
        @Part("lang") lang: RequestBody,
        @Part("name") name: RequestBody

        ): UserRegisterModel

    @Multipart
    @POST("Customerlogin")
    suspend fun userLogin(
        @Part("phone") phone: RequestBody,
        @Part("lang") lang: RequestBody

    ): UserLoginModel


    @Multipart
    @POST("addCar")
    suspend fun carRegister(
        @Part("uid") uid: RequestBody,
        @Part("nickName") nickName: RequestBody,
        @Part("PlateNo") PlateNo: RequestBody,
        @Part("carMake") carMake: RequestBody,
        @Part("carModel") carModel: RequestBody,
        @Part("year") year: RequestBody,
        @Part("lang") lang: RequestBody,
        @Part("jordanian") jordanian: RequestBody

    ): CarRegisterModel


    @Multipart
    @POST("myCars")
    suspend fun showMyCars(
        @Part("uid") uid: RequestBody?,
        @Part("lang") lang: RequestBody?

    ): MyCarsModel


    @Multipart
    @POST("viewUserProfile")
    suspend fun viewUserProfile(
        @Part("uid") uid: RequestBody,
        @Part("lang") lang: RequestBody

    ): UserProfileModel


    @Multipart
    @POST("updateUser")
    suspend fun updateUserInfo(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
//        @Part("image") image: MultipartBody.Part?

        ): UpdateUserInfoModel


    @Multipart
    @POST("ridesHistory")
    suspend fun historyRides(
        @Part("uid") uid: RequestBody

    ): HistoryRidesModel


    @Multipart
    @POST("deleteCar")
    suspend fun deleteCar(
        @Part("car_id") car_id: RequestBody,
        @Part("uid") uid: RequestBody

    ): RemoveCar


    @Multipart
    @POST("editCar")
    suspend fun editCar(
        @Part("car_id") car_id: RequestBody,
        @Part("lang") uid: RequestBody?,
        @Part("nick_name") nick_name: RequestBody?,
        @Part("car_make") car_make: RequestBody?,
        @Part("car_model") car_model: RequestBody?,
        @Part("year") year: RequestBody?,
        @Part("plate_no") plate_no: RequestBody?

    ): UpdateCar
//    carBackStatus


    @Multipart
    @POST("carBackStatus")
    suspend fun carBackStatus(
        @Part("uid") uid: RequestBody,
        @Part("lang") lang: RequestBody


    ): CarBackStatusModel


//    getMyCarBack

    @Multipart
    @POST("getMyCarBack")
    suspend fun callBackMyCar(
        @Part("uid") uid: RequestBody,
        @Part("lang") lang: RequestBody


    ): CallBackMyCarModel



    @Multipart
    @POST("CustomerStatus")
    suspend fun getCustomerStatusZero(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("seen") seen: RequestBody

    ): CustomerStatusZeroModel


    @Multipart
    @POST("CustomerStatus")
    suspend fun outOkTrip(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("outOK") outOK: RequestBody

    ): CustomerStatusThreeModel

    @Multipart
    @POST("CustomerStatus")
    suspend fun getCustomerStatusOne(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody

    ): CustomerStatusOneModel



    @Multipart
    @POST("CustomerStatus")
    suspend fun getCustomerStatusTwo(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody

    ): CustomerStatusTwoModel

    
    @Multipart
    @POST("CustomerStatus")
    suspend fun getCustomerStatusThree(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("seen") seen: RequestBody

    ): CustomerStatusThreeModel



    @Multipart
    @POST("setCarDefualt")
    fun getSetCarDefault(
        @Part("uid") uid: RequestBody,
        @Part("car_id") carId: RequestBody,
        @Part("lang") lang: RequestBody

        ): Call<SetCar>


    @Multipart
    @POST("CustomerStatus")
    suspend fun getCustomerStatus(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,

    ): CustomerStatusModel

    @Multipart
    @POST("CustomerStatus")
    suspend fun getCustomerStatusSeen(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("seen") seen: RequestBody

    ): CustomerStatusModel


    @Multipart
    @POST("logout")
    suspend fun getCustomerLogout(
        @Part("uid") uid: RequestBody

    ): Logout


    @Multipart
    @POST("getNotficaition")
    suspend fun getNotification(
        @Part("lang") lang: RequestBody,
        @Part("uid") uid: RequestBody


        ): NotificationResponse





    // StartRide?lang=ar&driver_id=527&customer_id=190&lat=1&lon=a (Get)
// interface ProductInterface {
//    @GET("products?")
//    suspend fun getTodos(@Query("key") key: String): Response<List<product>>
//}

//    @Multipart
//    @GET("StartRide")
//    suspend fun startRide(
//        @Query("lang") lang: String,
//        @Query("driver_id") driver_id: String,
//        @Query("customer_id") customer_id: String,
//        @Query("lat") lat: String,
//        @Query("lon") lon: String
//
//    ): StartRideModel


}