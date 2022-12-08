package com.example.safevalet.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.safevalet.HomeActivity
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentMapsBinding
import com.example.safevalet.databinding.MeetDriverBinding
import com.example.safevalet.databinding.UserProfileBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.UserData
import com.example.safevalet.viewmodel.UserViewModel
import retrofit2.HttpException
import java.io.File

class ProfileFragment: AppCompatActivity(){

    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(this)}

//    private var compressedImageFile: File? = null
private lateinit var binding: UserProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.user)

        if (HelperUtils.getLang(this) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }

        binding.toolbarInclude.menuNotfication.setOnClickListener{
//            onBackPressedDispatcher.onBackPressed()
//            startActivity(Intent(this,HomeActivity::class.java))
            onBackPressed()

        }

        binding.toolbarInclude.notficationBtn.hide()



        userVM.getUserInfo().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupUserInfo(result.data.data)
                        Log.i("m6o", "onViewCreated: $userID")


                        Glide.with(this)
                            .load(result.data.data.image)
                            .placeholder(R.drawable.ic_user_profile)
                            .error(R.drawable.ic_user_profile)
                            .into(binding.userImage)


                    } else {
                        Toast.makeText(this, result.data.status.msg, Toast.LENGTH_SHORT)
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



        userVM.updateUserInfoResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    Toast.makeText(this, result.data.status.msg, Toast.LENGTH_LONG)
                        .show()
                    if (result.data.status.status == 1) {
                        Log.i("TAG", "onViewCreated: " + result.data.status.msg)
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText( this,getString(R.string.error), Toast.LENGTH_LONG)
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