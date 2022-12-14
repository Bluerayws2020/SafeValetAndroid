package com.example.safevalet.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.safevalet.HomeActivity
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentHomeBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel
import retrofit2.HttpException


class HomeFragment: BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(applicationContext())}


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

        binding.showMyQRImg.setOnClickListener(this)
        binding.callBackMyCarImg.setOnClickListener(this)
        binding.exchange.setOnClickListener(this)

        val sharedPreferences = mContext?.getSharedPreferences(HelperUtils.SHARED_PREF, Context.MODE_PRIVATE)

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.home)


        val nickName = sharedPreferences?.getString("nickname", "")
        val plateNo = sharedPreferences?.getString("plateNo", "")

        if(nickName != null || plateNo != null) {
            "$nickName\t  - \t$plateNo".also { binding.whiteMyCar.text = it }
        }
        else if (nickName == null && plateNo == null){
            "My_Car".also { binding.whiteMyCar.text = it }
        }

        binding.toolbarInclude.notficationBtn.setOnClickListener {
            navController?.navigate(R.id.notificationFragment)
        }

//        binding.toolbarInclude.homeIcon.show()


        if(HomeActivity.comeFromRegister == 1){
            navController?.navigate(R.id.notificationFragment)
        }
        HomeActivity.comeFromRegister = 0

        Log.i("comeFromRegister", "onViewCreated: " + HomeActivity.comeFromRegister)


        userVM.getBackMyCarResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {

                        Log.i("m6o", "onViewCreated: $userID")
                        Toast.makeText(context, result.data.carBack, Toast.LENGTH_SHORT).show()

                        if(result.data.carBack == "0"){


                            binding.showCardView.strokeColor = Color.parseColor("#FF00B0FF")
                            binding.showCardView.setCardBackgroundColor(Color.parseColor("#FF00B0FF"))
                            binding.showCardView.setContentPadding(10,10,10,10)

//                            binding.progressBarUserInstitution.hide()

                        }

                        else {

                            binding.callBackCardView.strokeColor = Color.parseColor("#FF00B0FF")
                            binding.callBackCardView.setCardBackgroundColor(Color.parseColor("#FF00B0FF"))
                            binding.callBackCardView.setContentPadding(10,10,10,10)

//                            binding.progressBarUserInstitution.hide()


                        }


                    } else {
                        Toast.makeText(requireContext(), result.data.status.msg, Toast.LENGTH_SHORT)
                            .show()
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

        userVM.callBackMyCar(userID, language)




        userVM.getCustomerStatusThreeResponse().observe(viewLifecycleOwner){ result ->
            when(result){
                is NetworkResults.Success -> {
                    if(result.data.status.status == 3 || result.data.param == "in_status"){
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
                        doneBtn.setOnClickListener{
                            myDialog.dismiss()
                        }


                        Toast.makeText(requireContext(), result.data.status.msg.toString() , Toast.LENGTH_SHORT).show()
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



        binding.callBackMyCarImg.setOnClickListener{

            userVM.getCallBackCarResponse().observe(viewLifecycleOwner){ result ->
                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.status.status == 0) {
                            Toast.makeText(
                                requireContext(),
                                result.data.status.msg.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Log.i("Status", "onClick: " + result.data.status.msg.toString())
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

        userVM.callBackCarModel(userID, language)


        binding.showMyQRImg.setOnClickListener {

            userVM.getCustomerStatusResponse().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResults.Success -> {
                        when (result.data.status.status) {

                            0 -> {
                                userVM.getCustomerStatusZeroResponse()
                                    .observe(viewLifecycleOwner) { result ->
                                        when (result) {
                                            is NetworkResults.Success -> {
                                                if (result.data.status.status == 0 || result.data.status.status == 1) {

                                                    Toast.makeText(
                                                        requireContext(),
                                                        result.data.status.msg.toString(),
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    Navigation.findNavController(it)
                                                        .navigate(R.id.action_homeFragment_to_showMyCarFragment)
                                                }
                                            }
                                            is NetworkResults.Error -> {
                                                result.exception.printStackTrace()
                                                if (result.exception is HttpException)
                                                    Log.e(
                                                        "TAG",
                                                        "onViewCreated: ${result.exception}"
                                                    )
                                                else
                                                    Log.e("TAG", "onViewCreated: ERROR")
                                            }

                                        }

                                    }

                                userVM.customerStatusZeroModel(language, userID, "1")
                            }
//                            1 -> {
//                                userVM.getCustomerStatusOneResponse()
//                                    .observe(viewLifecycleOwner) { result ->
//                                        when (result) {
//                                            is NetworkResults.Success -> {
//                                                if (result.data.status.status == 1) {
//
//                                                    Toast.makeText(
//                                                        requireContext(),
//                                                        result.data.status.msg.toString(),
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//
//                                                    Navigation.findNavController(it)
//                                                        .navigate(R.id.action_homeFragment_to_showMyCarFragment)
//                                                }
//                                            }
//                                            is NetworkResults.Error -> {
//                                                result.exception.printStackTrace()
//                                                if (result.exception is HttpException)
//                                                    Log.e(
//                                                        "TAG",
//                                                        "onViewCreated: ${result.exception}"
//                                                    )
//                                                else
//                                                    Log.e("TAG", "onViewCreated: ERROR")
//                                            }
//
//                                        }
//
//                                    }
//
//                                userVM.customerStatusOneModel(language, userID)
//                            }

                            1 -> {
                                userVM.getCustomerStatusTwoResponse()
                                    .observe(viewLifecycleOwner) { result ->
                                        when (result) {
                                            is NetworkResults.Success -> {
                                                if (result.data.status.status == 2) {

                                                    Navigation.findNavController(it)
                                                        .navigate(R.id.action_homeFragment_to_mapFragment)
                                                }
                                            }
                                            is NetworkResults.Error -> {
                                                result.exception.printStackTrace()
                                                if (result.exception is HttpException)
                                                    Log.e(
                                                        "TAG",
                                                        "onViewCreated: ${result.exception}"
                                                    )
                                                else
                                                    Log.e("TAG", "onViewCreated: ERROR")
                                            }

                                        }

                                    }
                                userVM.customerStatusTwoModel(language, userID)

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
            userVM.customerStatusModel(language, userID)

        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.showMyQRImg -> {
//                Navigation.findNavController(v)
//                    .navigate(R.id.action_homeFragment_to_showMyCarFragment)

            }

            R.id.exchange -> {
//                Navigation.findNavController(v)
//                    .navigate(R.id.action_homeFragment_to_exchangeCarFragment)

                Navigation.findNavController(v)
                    .navigate(R.id.action_homeFragment_to_mapFragment)

            }

        }
    }
}
















//            userVM.getCustomerStatusZeroResponse().observe(viewLifecycleOwner){ result ->
//                when (result) {
//                    is NetworkResults.Success -> {
//                        when (result.data.status.status) {
//                            0 -> {
//
//                                Toast.makeText(requireContext(), result.data.status.msg.toString() , Toast.LENGTH_SHORT).show()
//
//                                Navigation.findNavController(it)
//                                    .navigate(R.id.action_homeFragment_to_showMyCarFragment)
//
//
//                            }
//
//                            1 -> {
//
//                                Toast.makeText(requireContext(), result.data.status.msg.toString() , Toast.LENGTH_SHORT).show()
//
//                                Navigation.findNavController(it)
//                                    .navigate(R.id.action_homeFragment_to_showMyCarFragment)
//
//                            }
//
//                            2 -> {
//
//                                // Map Fragment
//
//
//                                userVM.getCustomerStatusTwoResponse().observe(viewLifecycleOwner){ result ->
//                                    when (result) {
//                                        is NetworkResults.Success -> {
//                                            if (result.data.status.status == 2) {
//
//                                                Navigation.findNavController(it)
//                                                    .navigate(R.id.action_homeFragment_to_mapFragment)
//                                            }
//                                        }
//                                        is NetworkResults.Error -> {
//                                            result.exception.printStackTrace()
//                                            if (result.exception is HttpException)
//                                                Log.e("TAG", "onViewCreated: ${result.exception}")
//                                            else
//                                                Log.e("TAG", "onViewCreated: ERROR")
//                                        }
//
//                                    }
//
//                                }
//
//                                userVM.customerStatusTwoModel(language, userID)
//
////                                Toast.makeText(requireContext(), result.data.status.msg.toString() , Toast.LENGTH_SHORT).show()
//                                Log.i("JJJJ", "onViewCreated: " +  result.data.status.msg.toString())
//                            }
//                        }
//
//
//                        Log.i("Status", "onClick: " + result.data.status.msg.toString())
//                    }
//                    is NetworkResults.Error -> {
//                        result.exception.printStackTrace()
//                        if (result.exception is HttpException)
//                            Log.e("TAG", "onViewCreated: ${result.exception}")
//                        else
//                            Log.e("TAG", "onViewCreated: ERROR")
//                    }
//                }
//
//            }
//            }
//        }







//        binding.showCardViewIn.setOnClickListener{
//
//            userVM.getCustomerStatusTwoResponse().observe(viewLifecycleOwner){ result ->
//                when (result) {
//                    is NetworkResults.Success -> {
//                        when (result.data.status.status) {
//                            2 -> {
//
//                                // Map Fragment
//
//                                Navigation.findNavController(it)
//                                    .navigate(R.id.action_homeFragment_to_mapFragment)
//
//                                Toast.makeText(requireContext(), result.data.status.msg.toString() , Toast.LENGTH_SHORT).show()
//                                Log.i("JJJJ", "onViewCreated: " +  result.data.status.msg.toString())
//                            }
//                        }
//
//                        Log.i("Status", "onClick: " + result.data.status.msg.toString())
//
//                    }
//                    is NetworkResults.Error -> {
//                        result.exception.printStackTrace()
//                        if (result.exception is HttpException)
//                            Log.e("TAG", "onViewCreated: ${result.exception}")
//                        else
//                            Log.e("TAG", "onViewCreated: ERROR")
//                    }
//                }
//
//            }
//        }
//
//        userVM.customerStatusTwoModel(language, userID)






