package com.example.safevalet.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.HomeActivity
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentHomeBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext


class HomeFragment:BaseFragment<FragmentHomeBinding>(){

    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(applicationContext()) }
    private var latLocation: String? = ""
    private var longLocation: String? = ""
    private val locationPermissionCode = 2
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val handler = Handler()

    var timer :CountDownTimer? = null

    val dialog : Dialog?  = null
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        binding.layoutToolBar.show()

//        if (HelperUtils.getLang(context) == "ar"){
//            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))
//
//        }else {
//            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))
//
//        }


        val sharedPreferences =
            mContext?.getSharedPreferences(HelperUtils.SHARED_PREF, Context.MODE_PRIVATE)

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.home)


        binding.exchange.setOnClickListener {
            startActivity(Intent(applicationContext(),MyCarFragment::class.java))
        }


//        check Trip
        getCustomerStatus(binding.showMyQRImg)
//        callCar
//        callBackMyCar(binding.callBackMyCarImg)
//check card Status
        checkStatusOfCardTrip()
        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here
                handler.postDelayed(this, 3000)
                runInBackgroundAndUseInCallerThread()

            }
        }, 0)



        val nickName = sharedPreferences?.getString("nickname", "")
        val plateNo = sharedPreferences?.getString("plateNo", "")

        if (nickName != null || plateNo != null) {
            "$nickName\t  - \t$plateNo".also { binding.whiteMyCar.text = it }
        } else if (nickName == null && plateNo == null) {
            "My_Car".also { binding.whiteMyCar.text = it }
        }

        binding.toolbarInclude.notficationBtn.setOnClickListener {
        startActivity(Intent(context,NotificationFragment::class.java))
        }

//        binding.toolbarInclude.homeIcon.show()


        if (HomeActivity.comeFromRegister == 1) {
            startActivity(Intent(context,NotificationFragment::class.java))
        }
        HomeActivity.comeFromRegister = 0

        Log.i("comeFromRegister", "onViewCreated: " + HomeActivity.comeFromRegister)
        progressDialog()





//finisTrip
//        userVM.customerStatusThreeModel(language, userID)

//call car
        binding.callBackMyCarImg.setOnClickListener {
            binding.progresscallCar.show()
            userVM.callBackCarModel() }
        callBackMyCar()


//        cheeck status if user have trip to track or start New Trip By Scan Qr
//        binding.showCardView.setOnClickListener {  userVM.customerStatusChoseMapOrScan()  }
//        binding.showCardViewIn.setOnClickListener {  userVM.customerStatusChoseMapOrScan()  }
        binding.showMyQRImg .setOnClickListener {
            binding.progresscallshowQR.show()
            dialog?.show()

            userVM.customerStatusChoseMapOrScan() }



    }
//function
    private fun callBackMyCar() {
    dialog?.show()
        userVM.getCallBackCarResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    binding.progresscallshowQR.hide()
                    binding.progresscallCar.hide()

                    Toast.makeText(
                        requireContext(),
                        result.data.status.msg.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (result.data.status.status == 1) {
                        dialog?.hide()

                        startActivity(Intent(context,MapsFragment::class.java))

                    }else {

                    }
                    Log.i("Status", "c: " + result.data.status.msg.toString())
                }
                is NetworkResults.Error -> {
                    binding.progresscallshowQR.hide()
                    binding.progresscallCar.hide()

                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        Log.e("TAG", "onViewCreated: ${result.exception}")
                    else
                        Log.e("TAG", "onViewCreated: ERROR")
                }
            }
        }

    }

    private fun getCustomerStatus(btn:ImageView) {
        userVM.getCustomerStatusResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    binding.progresscallshowQR.hide()
                    dialog?.hide()

                    var msg = ""
                    if (HelperUtils.getLang(context) == "ar") {
                        msg = "لا يوجد جولات حتى الان"
                    } else {
                        msg = "No rides until now"
                    }
                    Toast.makeText(
                        requireContext(),
                        result.data.status.msg.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (result.data.status.status == 0   ) {
                        Log.d("Here","Iam ")
                        startActivity(Intent(context,ShowMyCarFragment::class.java))


                    } else {

                        Navigation.findNavController(btn)
                        startActivity(Intent(context,MapsFragment::class.java))
                    }

                }
                is NetworkResults.Error -> {
                    binding.progresscallshowQR.hide()
                    binding.progresscallshowQR.hide()

                    result.exception.printStackTrace()
                }
            }
        }


    }


    private fun finishTrip(){
    userVM.getCustomerStatusThreeResponse().observe(viewLifecycleOwner) { result ->
        when (result) {
            is NetworkResults.Success -> {
                if (result.data.status.status == 3 || result.data.param == "in_status") {
                    // pop up window
                    val dialogBinding = layoutInflater.inflate(R.layout.popup_window, null)

                    val myDialog = Dialog(requireContext())
                    myDialog.setContentView(dialogBinding)


                    myDialog.setCancelable(true)
                    myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    myDialog.show()

                    val estTime = dialogBinding.findViewById<TextView>(R.id.estTime)
                    estTime.text = result.data.status.msg.toString()


                    val doneBtn = dialogBinding.findViewById<TextView>(R.id.Done)
                    doneBtn.setOnClickListener {
                        myDialog.dismiss()
                    }


                    Toast.makeText(
                        requireContext(),
                        result.data.status.msg.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
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


    private fun checkStatusOfCardTrip(){
        userVM.getBackMyCarResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {

                        Log.d("statuts Car Bcck", result.data.carBack)
//                        Toast.makeText(context, result.data.carBack, Toast.LENGTH_SHORT).show()

                        if (result.data.carBack == "0") {


                            binding.showCardView.strokeColor = Color.parseColor("#FF00B0FF")
                            binding.showCardView.setCardBackgroundColor(Color.parseColor("#FF00B0FF"))
                            binding.showCardView.setContentPadding(10, 10, 10, 10)

                            binding.callBackCardView.strokeColor = Color.parseColor("#C5C5C5")
                            binding.callBackCardView.setCardBackgroundColor(Color.parseColor("#C5C5C5"))
                            binding.callBackCardView.setContentPadding(10, 10, 10, 10)
//                            binding.progressBarUserInstitution.hide()

                        } else {

                            binding.callBackCardView.strokeColor = Color.parseColor("#FF00B0FF")
                            binding.callBackCardView.setCardBackgroundColor(Color.parseColor("#FF00B0FF"))
                            binding.callBackCardView.setContentPadding(10, 10, 10, 10)

//                            binding.progressBarUserInstitution.hide()
                            binding.showCardView.strokeColor = Color.parseColor("#C5C5C5")
                            binding.showCardView.setCardBackgroundColor(Color.parseColor("#C5C5C5"))
                            binding.showCardView.setContentPadding(10, 10, 10, 10)


                        }


                    } else {
                        Log.d("m6o", "onViewCreated: $userID")

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
    // background
    private fun runInBackgroundAndUseInCallerThread() {

        GlobalScope.launch {
            val dispatcher = getDispatcherFromCurrentThread(this)
            CoroutineScope(dispatcher).launch {


                userVM.carBackStatus()

            }
        }
    }


    private fun getDispatcherFromCurrentThread(scope: CoroutineScope): CoroutineContext {
        return scope.coroutineContext
    }


    private fun progressDialog(){
    val dialog = Dialog(requireContext(), com.example.safevalet.R.style.CustomProgressDialog)
    val customView = ImageView(activity)
    customView.setBackgroundResource(com.example.safevalet.R.drawable.logo)
    customView.setImageDrawable(resources.getDrawable(com.example.safevalet.R.drawable.logo))
}
}