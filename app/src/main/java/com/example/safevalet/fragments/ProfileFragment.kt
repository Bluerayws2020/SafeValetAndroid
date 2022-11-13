package com.example.safevalet.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.R
import com.example.safevalet.databinding.UserProfileBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.UserData
import com.example.safevalet.viewmodel.UserViewModel
import retrofit2.HttpException
import java.io.File

class ProfileFragment: BaseFragment<UserProfileBinding>(){

    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(applicationContext())}


//    private var compressedImageFile: File? = null


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): UserProfileBinding {
        return UserProfileBinding.inflate(inflater, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        userVM.getUserInfo().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupUserInfo(result.data.data)
                        Log.i("m6o", "onViewCreated: $userID")

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



        userVM.updateUserInfoResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    Toast.makeText(requireContext(), result.data.status.msg, Toast.LENGTH_LONG)
                        .show()
                    if (result.data.status.status == 1) {
                        Log.i("TAG", "onViewCreated: " + result.data.status.msg)
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding.updatebtn.setOnClickListener{
//            HelperUtils.hideKeyBoard()

            userVM.updateUserProfile(
                language = "ar",
                userId = userID,
                binding.yourName.text.trim().toString(),
                binding.mobile.text.trim().toString(),
//                compressedImageFile
            )
        }


    }


    private fun setupUserInfo(profile: UserData) {
        binding.yourName.setText(profile.userName.toString())
        binding.yourEmail.setText(profile.type.toString())
        binding.mobile.setText(profile.phone.toString())
    }


}