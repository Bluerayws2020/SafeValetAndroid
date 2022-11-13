package com.example.safevalet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
}

data class UserRegisterModel(

    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val user: UserModel

    )

data class MessageModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String
)

data class UserModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("car") val car: String
)


data class UserLoginModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: LoginModel
)


data class LoginModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("car") val car: CarLoginModel
)

data class CarLoginModel(
    @SerializedName("title") val title: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("carMake") val carMake: String,
    @SerializedName("carModel") val carModel: String,
    @SerializedName("palteNo") val palteNo: String,
    @SerializedName("year") val year: String

)


data class CarRegisterModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val car: CarModel

)

data class CarModel(
    @SerializedName("carID") val carID: String,
    @SerializedName("carNickName") val carNickName: String,
    @SerializedName("carNumber") val carNumber: String

)

@Parcelize
data class MyCarsDataModel(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("carMake") val carMake: String,
    @SerializedName("carModel") val carModel: String,
    @SerializedName("palteNo") val palteNo: String,
    @SerializedName("year") val year: String,
    @SerializedName("field_isdefualt") val field_isdefualt: String

):Parcelable

data class MyCarsModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("cars") val cars: ArrayList<MyCarsDataModel>
)


data class UserProfileModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: UserData

    )

data class UserData(
    @SerializedName("uid") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("mail") val mail: String,
    @SerializedName("image") val image: String,

    )



data class UpdateUserInfoModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: UserData

)


data class HistoryRidesModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: List<HistoryData>
)


data class HistoryData(
    @SerializedName("type") val type: String,
    @SerializedName("carName") val carName: String,
    @SerializedName("carID") val carID: String,
    @SerializedName("date") val date: String,
    @SerializedName("dropStart") val dropStart: DropStart,
    @SerializedName("dropEnd") val dropEnd: DropEnd,
    @SerializedName("driver1") val driver1: String,
    @SerializedName("phone1") val phone1: String,
    @SerializedName("station_responsible1") val station_responsible1: String,
    @SerializedName("driver_image1") val driver_image1: String,
    @SerializedName("time1") val time1: String,
    @SerializedName("driver2") val driver2: String,
    @SerializedName("phone2") val phone2: String,
    @SerializedName("station_responsible2") val station_responsible2: String,
    @SerializedName("station_responsible_phone2") val station_responsible_phone2: String,
    @SerializedName("driver_image2") val driver_image2: String,
    @SerializedName("time2") val time2: String,

    )

data class DropStart(
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String

    )

data class DropEnd(
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String
)




data class RemoveCar(
    @SerializedName("msg") val status: MessageModel

    )

data class UpdateCar(
    @SerializedName("msg") val status: MessageModel

)
