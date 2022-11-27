package com.example.safevalet.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.safevalet.R
import com.example.safevalet.databinding.UserProfileBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.UserData
import com.example.safevalet.viewmodel.UserViewModel
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File

class ProfileFragment: BaseFragment<UserProfileBinding>(){

    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(applicationContext())}


    companion object{
        const val IMAGE_REQUEST_CODE = 100
    }

    private var mediaPath: String? = null
    private var postPath: String? = null

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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.user)

        if (HelperUtils.getLang(applicationContext()) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }

        binding.toolbarInclude.menuNotfication.setOnClickListener{
//            onBackPressedDispatcher.onBackPressed()
            navController?.navigate(R.id.homeFragment)
        }

        binding.toolbarInclude.notficationBtn.hide()



        userVM.getUserInfo().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupUserInfo(result.data.data)
                        Log.i("m6o", "onViewCreated: $userID")


                        Glide.with(applicationContext())
                            .load(result.data.data.image)
                            .placeholder(R.drawable.ic_user_profile)
                            .error(R.drawable.ic_user_profile)
                            .into(binding.userImage)


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

        binding.profileImage.setOnClickListener {
            Toast.makeText(applicationContext(), "dima", Toast.LENGTH_SHORT).show()
            pickImageGallery()
        }


    }


    private fun setupUserInfo(profile: UserData) {
        binding.yourName.setText(profile.userName.toString())
        binding.yourEmail.setText(profile.type.toString())
        binding.mobile.setText(profile.phone.toString())
    }


    @SuppressLint("IntentReset")
    private fun pickImageGallery() {

        activity?.let {
            if (ContextCompat.checkSelfPermission(it.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
            else {
                val pickImageIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                )
                pickImageIntent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(pickImageIntent, IMAGE_REQUEST_CODE)

            }
        }
    }

    private val PERMISSIONS_LENGTH = 2
    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<out String>, grantResults:IntArray) {
        // Check whether requestCode is set to the value of CAMERA_REQ_CODE during permission application, and then check whether the permission is enabled.

        if(requestCode == 1 )
        {
            if(grantResults.isNotEmpty() && grantResults.size
                == PERMISSIONS_LENGTH && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED)
            {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
                Log.i("REQUEST2CODE", "onActivityResult: $requestCode")
            }
        }else {
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("Recycle")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        Log.i("REQUEST CODE", "onActivityResult: $requestCode")
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            if (data != null) {
                // Get the Image from data
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)


                val cursor = context?.contentResolver?.query(
                    selectedImage!!,
                    filePathColumn,
                    null,
                    null,
                    null
                )
                assert(cursor != null)
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                mediaPath = cursor.getString(columnIndex)


                binding.userImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                postPath = mediaPath

                Log.i("postPath", "onActivityResult:  " + mediaPath.toString())

                val imageFile = File(mediaPath.toString())


                lifecycleScope.launch {
                    val compressedImageFile =
                        Compressor.compress(requireContext(), imageFile, Dispatchers.IO)
                    userVM.updateUserProfile(
                        language,
                        userId = userID.toString(),
                        name = binding.yourName.text.trim().toString(),
                        phone = binding.mobile.text.trim().toString(),
                        profileImage = compressedImageFile
                    )
                }

            } else if (resultCode != Activity.RESULT_CANCELED) {
                Toast.makeText(
                    requireContext(),
                    "Sorry, there was an error!",
                    Toast.LENGTH_LONG
                ).show()
            }

        } }


}