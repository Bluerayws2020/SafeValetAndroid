package com.example.safevalet.api

import com.example.safevalet.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {


    @Multipart
    @POST("SignUp")
    suspend fun userRegister(
        @Part("phone") phone: RequestBody,
        @Part("lang") lang: RequestBody

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
        @Part("lang") lang: RequestBody

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


}