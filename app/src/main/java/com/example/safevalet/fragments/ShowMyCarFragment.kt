package com.example.safevalet.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentMapsBinding
import com.example.safevalet.databinding.MeetDriverBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext


class ShowMyCarFragment: AppCompatActivity(), View.OnClickListener {


    private var navController: NavController? = null
    private val userID by lazy { HelperUtils.getUID(this)}
    private val userVM by viewModels<UserViewModel>()

var timer:CountDownTimer? = null
    private lateinit var binding: MeetDriverBinding


    val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MeetDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, Context.MODE_PRIVATE)


        binding.toolbarInclude.toolbar.title = resources.getString(R.string.show_my_qr)
//Start Ride
        redirectToTrackCar()


        val nickName = sharedPreferences?.getString("nickname", "")
        val plateNo = sharedPreferences?.getString("plateNo", "")

        if(nickName != null || plateNo != null) {
            "$nickName\t  - \t$plateNo".also { binding.whiteMyCar.text = it }
        }
        else if (nickName == null && plateNo == null){
            "My_Car".also { binding.whiteMyCar.text = it }
        }

        binding.toolbarInclude.notficationBtn.setOnClickListener {
            startActivity(Intent(this,NotificationFragment::class.java))

        }
//        binding.toolbarInclude.homeIcon.hide()

        //initializing MultiFormatWriter for QR code
        val mWriter = MultiFormatWriter()
        try {
            //BitMatrix class to encode entered text and set Width & Height
            val mMatrix =
                mWriter.encode(userID, BarcodeFormat.QR_CODE, 400, 400)
            val mEncoder = BarcodeEncoder()
            val mBitmap =
                mEncoder.createBitmap(mMatrix) //creating bitmap of code
            binding.QRImg.setImageBitmap(mBitmap) //Setting generated QR code to imageView

        } catch (e: WriterException) {
            e.printStackTrace()
        }


        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here
                handler.postDelayed(this, 3000)
                runInBackgroundAndUseInCallerThread()

            }
        }, 0)



    }









    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.QRImg -> {
            }


        }
    }

//    customerStatusOneModel
    fun redirectToTrackCar(){


        userVM.getCustomerStatusResponse().observe(this){ result ->
            when (result) {
                is NetworkResults.Success -> {

if (result.data.status.newStatus == 1){

startActivity(Intent(this,MapsFragment::class.java))

}
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        Log.e("TAG", "onViewCreated: ${result.exception}")
                    else
                        Log.e("TAG", "onViewCreated: ERROR")
                }

            }
        }
    }


    // background Jop
    fun runInBackgroundAndUseInCallerThread() {

        GlobalScope.launch {
            val dispatcher = getDispatcherFromCurrentThread(this)
            CoroutineScope(dispatcher).launch {



                userVM.customerStatusChoseMapOrScan()

            }
        }
    }



//    Back Ground Work

    fun getDispatcherFromCurrentThread(scope: CoroutineScope): CoroutineContext {
        return scope.coroutineContext
    }

}