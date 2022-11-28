package com.example.safevalet.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment

import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentMapBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.HttpException

class MapsFragment : Fragment()  {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(context)}
    private lateinit var binding: FragmentMapBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.root
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val driverName = view.findViewById<TextView>(R.id.driverName)
        val driverJob = view.findViewById<TextView>(R.id.driverJob)
        val carName = view.findViewById<TextView>(R.id.carName)
        val carStatus = view.findViewById<TextView>(R.id.carStatus)
        val driverMobile = view.findViewById<ImageView>(R.id.driverMobile)
        val driverImg = view.findViewById<ImageView>(R.id.driverImg)
        val cameraImg = view.findViewById<ImageView>(R.id.carImg)

        navController = Navigation.findNavController(view)


//
        userVM.getCustomerStatusTwoResponse().observe(viewLifecycleOwner){ result ->
            when (result) {
                is NetworkResults.Success -> {

                    driverName.text = result.data.data.name.toString()
                    driverJob.text = result.data.data.type.toString()
                    carName.text = result.data.data.carName.toString()

                    Glide.with(requireContext())
                        .load(HelperUtils.BASE_URL + result.data.data.driver_image)
                        .placeholder(R.drawable.ic_user_profile)
                        .error(R.drawable.ic_user_profile)
                        .into(driverImg)

                    if (result.data.param == "seen" || result.data.param == "outOK")
                    {
                        carStatus.text = "Parked"
                    }
                    else
                    {
                        carStatus.text = "Parking in Progress"
                    }


                    driverMobile.setOnClickListener{
                        Toast.makeText(requireContext(), result.data.data.phone, Toast.LENGTH_SHORT).show()
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

        userVM.customerStatusTwoModel(language, userID)



        userVM.getCustomerStatusThreeResponse().observe(viewLifecycleOwner){ result ->
            when (result) {
                is NetworkResults.Success -> {

                    driverName.text = result.data.data.name.toString()
                    driverJob.text = result.data.data.type.toString()
                    carName.text = result.data.data.carName.toString()

                    Glide.with(requireContext())
                        .load(result.data.data.driver_image)
                        .placeholder(R.drawable.ic_user_profile)
                        .error(R.drawable.ic_user_profile)
                        .into(driverImg)


                    if(result.data.status.status == 3 || result.data.param == "in_status") {
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

                        if (result.data.param == "seen" || result.data.param == "outOK") {
                            carStatus.text = "Parked"
                        } else {
                            carStatus.text = "Parking in Progress"
                        }


                        driverMobile.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                result.data.data.phone,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

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


        userVM.customerStatusThreeModel(language, userID)


        cameraImg.setOnClickListener {
            userVM.getCustomerStatusThreeResponse().observe(viewLifecycleOwner){ result ->
                when (result) {
                    is NetworkResults.Success -> {

                        val dialogBinding2 =
                            layoutInflater.inflate(R.layout.popup_window_car_image, null)


                        val myDialog2 = Dialog(requireContext())
                        myDialog2.setContentView(dialogBinding2)

                        val carImg = dialogBinding2.findViewById<ImageView>(R.id.aImg)


                        if (result.data.car.image.isNotEmpty()) {
                            Glide.with(requireContext())
                                .load(result.data.car.image)
                                .placeholder(R.drawable.ic_user_profile)
                                .error(R.drawable.ic_user_profile)
                                .into(carImg)
                        }

                        myDialog2.setCancelable(true)
                        myDialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        myDialog2.show()



                        val doneeBtn = dialogBinding2.findViewById<TextView>(R.id.Done)
                        doneeBtn.setOnClickListener {
                            myDialog2.dismiss()
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

            userVM.customerStatusThreeModel(language, userID)

        }



    }

}