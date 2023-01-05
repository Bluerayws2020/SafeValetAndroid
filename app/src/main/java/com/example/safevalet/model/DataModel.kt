package com.example.safevalet.model

import android.os.Parcelable
import com.example.safevalet.R
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
    ,    @SerializedName("newStatus") val newStatus: Int? = null

)

data class UserModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone") val phone: String,
//    @SerializedName("car") val car: String
)


data class UserLoginModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: LoginModel
)

@Parcelize
data class LoginModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone") val phone: String,
//    @SerializedName("car") val car: CarLoginModel
) : Parcelable

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

data class BaseArrayModel<T>(

    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<T>
)
data class UIDData(

    @SerializedName("uid") val uid: Int
)

sealed class OTPInfo : Parcelable {
    abstract var phoneNumber: String
    abstract val otpType: Int

    @Parcelize
    data class LoginOTP(
        val loginData: LoginModel,
        val userPassword: String,
        val isRemembered: Boolean,
        override var phoneNumber: String,
        override val otpType: Int,
    ) : OTPInfo()

    @Parcelize
    data class SignUpOTP(
        override var phoneNumber: String,
        override val otpType: Int,
    ) : OTPInfo()

    @Parcelize
    data class ForgetPassOTP(
        override var phoneNumber: String,
        override val otpType: Int,
    ) : OTPInfo()
}

@Parcelize
data class MyCarsDataModel(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("carMake") val carMake: String,
    @SerializedName("carModel") val carModel: String,
    @SerializedName("palteNo") val palteNo: String,
    @SerializedName("year") val year: String,
    @SerializedName("field_isdefualt") var field_isdefualt: String

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

data class CarBackStatusModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("carBack") val carBack: String
)

data class CallBackMyCarModel(
    @SerializedName("msg") val status: MessageModel
)



//{
//    "msg": {
//    "status": 0,
//    "message": "لا يوجد جولات حتى الان"
//},
//    "data": ""
//}



data class CustomerStatusModel(
    @SerializedName("msg") val status: MessageModel,

)

data class CustomerStatusZeroModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: String

)


data class CustomerStatusOneModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: DataModel? = null
,    @SerializedName("newStatus") val newStatus: Int? = null

)

data class CustomerStatusTwoModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: DataModel,
    @SerializedName("param") val param: String

)


data class CustomerStatusThreeModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: DataModel,
    @SerializedName("car") val car: CarThree,
    @SerializedName("param") val param: String,

    @SerializedName("newStatus") val newStatus: Int? = null

)


data class CarThree(
    @SerializedName("image") val image: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String

)

data class DataModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("carName") val carName: String,
    @SerializedName("carID") val carID: String,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("station_responsible") val station_responsible: String,
    @SerializedName("station_responsible_phone") val station_responsible_phone: String,
    @SerializedName("driver_image") val driver_image: String

)


data class SetCar(
    @SerializedName("msg") val status: MessageModel,
    var flag: Boolean = true
)


data class Logout(
    @SerializedName("msg") val status: MessageModel
    )

//name = arrayListOf<String>("My Car","History", "Share", "My Profile", "Settings", "Language", "Logout")
//val image = arrayListOf<Int>(
//    R.drawable.car, R.drawable.history, R.drawable.share,
//    R.drawable.user, R.drawable.settings, R.drawable.translating, R.drawable.logout)
data class MenuName(
    val MyCar: String = "My Car",
    val History: String = "History",
    val Share: String = "Share",
    val Profile: String = "My Profile",
    val Settings: String = "Settings",
    val Language: String = "Language",
    val Logout: String = "Logout"

)

data class MenuImage(
    val car: Int = R.drawable.car,
    val history: Int = R.drawable.history,
    val share: Int = R.drawable.share,
    val user: Int = R.drawable.user,
    val settings: Int = R.drawable.settings,
    val translating: Int = R.drawable.translating,
    val logout: Int = R.drawable.logout
)



data class NotificationResponse (

    @SerializedName("msg"  ) var msg  :MessageModel,
    @SerializedName("data" ) var notification_data : ArrayList<ArrayList<NotificationDataResponse>> = arrayListOf()

)

data class NotificationDataResponse (


    @SerializedName("id"      ) var id     : String? = null,
    @SerializedName("bodyEN"  ) var bodyEN : String? = null,
    @SerializedName("bodyAR"  ) var bodyAR : String? = null,
    @SerializedName("date"    ) var date   : String? = null,
    @SerializedName("time"    ) var time   : String? = null,
    @SerializedName("uid"     ) var uid    : String? = null,
    @SerializedName("flag"    ) var flag   : String? = null,
    @SerializedName("ride_id" ) var rideId : String? = null,
    @SerializedName("msg_id"  ) var msgId  : String? = null
)