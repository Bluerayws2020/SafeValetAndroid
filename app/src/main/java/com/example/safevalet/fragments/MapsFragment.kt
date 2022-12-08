package com.example.safevalet.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentMapsBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.androidgamesdk.gametextinput.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext


class MapsFragment : AppCompatActivity(),OnMapReadyCallback {
var carStatus:TextView?=null


    private lateinit var currantLocation:Location
    private var longLocation: Double? = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var timer:CountDownTimer? =null

    private lateinit var mMap: GoogleMap

    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(this)}
    private lateinit var binding: FragmentMapsBinding
    private lateinit var locationRequest: LocationRequest
var callbacks:OnMapReadyCallback? = null
    val handler = Handler()

    override fun onStart() {
        super.onStart()
        locationSettings()

    }
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//binding.maps = mMap
    binding.toolbarInclude.notficationBtn.setOnClickListener {
        startActivity(Intent(this,NotificationFragment::class.java))

    }
    binding.toolbarInclude.menuNotfication.setOnClickListener{
        onBackPressed()
    }
    binding.toolbarInclude.toolbar.title = resources.getString(R.string._3_track_your_car)
    if (HelperUtils.getLang(this) == "ar"){
        binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

    }else {
        binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

    }

    binding.toolbarInclude.notficationBtn.hide()

    binding.toolbarInclude.menuNotfication.setOnClickListener{
        onBackPressed()
    }




    userVM.customerStatusThreeModel()




        userVM.customerStatusTwoModel(language, userID)
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    binding.maps.onCreate(savedInstanceState)

    binding.maps.getMapAsync(this)

    getCustomerStatus()


//call check car can back status ecah 3 second
    handler.postDelayed(object : Runnable {
        override fun run() {
            //Call your function here
            handler.postDelayed(this, 3000)
            runInBackgroundAndUseInCallerThread()

        }
    }, 0)


}









// background Thread
fun runInBackgroundAndUseInCallerThread() {

    GlobalScope.launch {
        val dispatcher = getDispatcherFromCurrentThread(this)
        CoroutineScope(dispatcher).launch {

//            carStatus?.text = result.data.status.msg.toString()


            userVM.customerStatusThreeModel()



        }
    }
}



//    Back Ground Work

    fun getDispatcherFromCurrentThread(scope: CoroutineScope): CoroutineContext {
        return scope.coroutineContext
    }

    fun tripEndd(msg:String){
        Log.d("Msg",msg)
        val dialogBinding = layoutInflater.inflate(R.layout.popup_window, null)

        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)


        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val estTime = dialogBinding.findViewById<TextView>(R.id.estTime)
        estTime.text = msg

        chechIfTripend()

        val doneBtn = dialogBinding.findViewById<TextView>(R.id.Done)
        doneBtn.setOnClickListener {
            myDialog.dismiss()
//            call Api
            userVM.outOkRequest()



        }
    }



    fun chechIfTripend(){
        userVM.retreveOutOk().observe(this){ result ->
            when (result) {
                is NetworkResults.Success -> {
                    Toast.makeText(this, result.data.status.msg.toString(), Toast.LENGTH_SHORT).show()

                    if (result.data.status.status == 1 && result.data.data != null){


                      onBackPressed()

                    }else{

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



    // Request location permissions
    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isEnabled = false
            permissions.entries.forEach {
                isEnabled = it.value
            }
            if (isEnabled) {
                enableUserLocationUI()
            }
        }


    private fun enableUserLocationUI() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }


//    ask for location on
    private fun locationSettings() {
    locationRequest = LocationRequest.create()
    locationRequest.apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 2 * 1000
        fastestInterval = 1 * 1000
    }

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    val client: SettingsClient = LocationServices.getSettingsClient(this)
    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//    binding.maps.onCreate(savedInstanceState)

    task.addOnSuccessListener {




    }

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(this, 101)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    fun getCustomerStatus() {
        Log.d("Her", "§")

        userVM.getCustomerStatusThreeResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    timer?.start()

                    binding.driverName.text = result.data.data.name.toString()
                    binding.driverJob.text = result.data.data.type.toString()
                    binding.carName.text = result.data.data.carName.toString()
                    Log.d("STATATA", result.data.status.newStatus.toString())

                    Glide.with(this)
                        .load(result.data.data.driver_image)
                        .placeholder(R.drawable.ic_user_profile)
                        .error(R.drawable.ic_user_profile)
                        .into(binding.driverImg)

                    binding.carStatus.text = result.data.status.msg.toString()


//                    car_image
                    binding.carImg.setOnClickListener {

                        val dialogBinding2 =
                            layoutInflater.inflate(R.layout.popup_window_car_image, null)

                        val myDialog2 = Dialog(this)
                        myDialog2.setContentView(dialogBinding2)


                        myDialog2.setCancelable(true)
                        myDialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        myDialog2.show()


                        val carImg = dialogBinding2.findViewById<ImageView>(R.id.aImg)

                        if (result.data.car.image.isNotEmpty()) {
                            Glide.with(this)
                                .load(result.data.car.image)
                                .placeholder(R.drawable.ic_user_profile)
                                .error(R.drawable.ic_user_profile)
                                .into(carImg)
                        }


                        val doneeBtn = dialogBinding2.findViewById<TextView>(R.id.Done)
                        doneeBtn.setOnClickListener {
                            myDialog2.dismiss()
                        }

                    }




//trip end
                    if (result.data.status.newStatus == 6 && result.data.status.status == 2) {

                        timer?.onFinish()

                        handler.removeMessages(0)
                        tripEndd(result.data.status.msg)
                        Log.d("Her", "§!131")
                    }


//driver mobile phone
                    binding.driverMobile.setOnClickListener {

                        val intent = Intent(Intent.ACTION_DIAL)
                        val uri = Uri.parse("tel:" + result.data.data.phone)
                        intent.data = uri
                        startActivity(intent)

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

    override fun onBackPressed() {
        super.onBackPressed()
        timer?.onFinish()
    }

//    override fun onMapReady(p0: GoogleMap) {
//    val latLang = LatLng(currantLocation.longitude,currantLocation.latitude)
//        val markerOptions  = MarkerOptions().position(latLang).title("My Adress")
//        p0.animateCamera(CameraUpdateFactory.newLatLng(latLang))
//        p0.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang,7f))
//        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, com.example.safevalet.R.raw.map_style)
//        p0.setMapStyle(mapStyleOptions)
//        p0.addMarker(markerOptions)
//
//    }

    override fun onResume() {
        super.onResume()
        binding.maps.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap) {


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        if (googleMap.isMyLocationEnabled == true) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
//                        currantLocation.longitude = location!!.longitude
//                        currantLocation.latitude = location!!.latitude

                    googleMap.isMyLocationEnabled = true
                    googleMap.uiSettings.isMyLocationButtonEnabled = true

                    val sydney = LatLng(location!!.latitude, location.longitude)
                    val markerOptions = MarkerOptions().position(sydney).title("My Adress")
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(sydney))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f))
                    val mapStyleOptions =
                        MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
                    googleMap.setMapStyle(mapStyleOptions)
                    googleMap.addMarker(markerOptions)


                }


        }else {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            Toast.makeText(
                this,
                "يرجى تشغيل خدمات الموقع للوصول الى الموقع الخاص بك",
                Toast.LENGTH_SHORT
            ).show()
            val sydney = LatLng(33.12312, 35.13123)
            val markerOptions = MarkerOptions().position(sydney).title("My Adress")
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(sydney))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f))
            val mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            googleMap.setMapStyle(mapStyleOptions)
            googleMap.addMarker(markerOptions)
        }

//        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)

//// Setting Dialog Title
//
//// Setting Dialog Title
//        alertDialog.setTitle("GPS is settings")
//// Setting Dialog Message
//// Setting Dialog Message
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")
//// On pressing Settings button
//// On pressing Settings button
//        alertDialog.setPositiveButton("g",
//            DialogInterface.OnClickListener { dialog, which ->
//                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//               startActivity(intent)
//            })
//
//// on pressing cancel button
//
//// on pressing cancel button
//        alertDialog.setNegativeButton("Cancel",
//            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
//// Showing Alert Message
//// Showing Alert Message
//        alertDialog.show()




    }


    }

