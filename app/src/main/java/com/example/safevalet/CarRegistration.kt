package com.example.safevalet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.safevalet.databinding.BookPlaceBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.isInputEmpty
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.CarModel
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel
import retrofit2.HttpException

class CarRegistration:  AppCompatActivity(){  //, View.OnClickListener {

    private lateinit var binding: BookPlaceBinding
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BookPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        val user_id = sharedPreferences.getString("uid", null)
        val intent = Intent.getIntent("")
        val flag = this@CarRegistration.intent.extras!!.getString("flag")
        Log.i("Flag 00", "onCreate: $flag")

        userVM.getCarRegisterResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        saveCarData(
                            result.data.car
                        )
                        val intentSignIn = Intent(this, HomeActivity::class.java)
                        startActivity(intentSignIn)
                        finishAffinity()
                    } else {
                        Toast.makeText(
                            this@CarRegistration,
                            result.data.status.msg,
                            Toast.LENGTH_SHORT
                        ).show()
//                        Log.i("TAG", "onCreate: " + result.data.user.car)
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                    if (result.exception is HttpException)
                        showMessage(result.exception.message())
                    else
                        showMessage("No Internet connection")
                }
            }

        }
        val carId = this@CarRegistration.intent.extras!!.getString("carID")
        val nickname = this@CarRegistration.intent.extras!!.getString("nickname")
        val carMake = this@CarRegistration.intent.extras!!.getString("carMake")
        val carModel = this@CarRegistration.intent.extras!!.getString("carModel")
        val year = this@CarRegistration.intent.extras!!.getString("year")
        val plateNo = this@CarRegistration.intent.extras!!.getString("plateNo")

        if (flag != "0")
        {
            binding.nickname.setText(nickname.toString())
            binding.carMake.setText(carMake.toString())
            binding.carModel.setText(carModel.toString())
            binding.year.setText(year.toString())
            binding.plateNo.setText(plateNo.toString())
        }

            Log.i("user_ID", "onCreate: $user_id")

        binding.updatebtn.setOnClickListener {
            HelperUtils.hideKeyBoard(this)
            if (isInputValid()) {
//                binding.progressBarLogin.show()
//                it.inVisible()
                if(flag == "0") {
                    userVM.carRegister(
                        user_id.toString(),
                        binding.nickname.toString(),
                        binding.plateNo.toString(),
                        language
                    )
                    Toast.makeText(applicationContext, "Car Registered", Toast.LENGTH_SHORT).show()
                }
                else{

                    userVM.updateCar(carId.toString(),
                        language,
                        binding.nickname.text.trim().toString(),
                        binding.carMake.text.trim().toString(),
                        binding.carModel.text.trim().toString(),
                        binding.year.text.trim().toString(),
                        binding.plateNo.text.trim().toString()
                        )

                    Toast.makeText(applicationContext, "Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }



    private fun isInputValid(): Boolean {
        var status = true

        if (binding.nickname.isInputEmpty()) {
            status = false
            binding.nickname.error ?: getString(R.string.Required)
        }

        if (binding.plateNo.isInputEmpty()) {
            status = false
            binding.plateNo.error ?: getString(R.string.Required)
        }

        return status
    }

    private fun saveCarData(car: CarModel) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("carId", car.carID.toString())
            putString("plateNo", car.carNumber.toString())

        }.apply()
    }

    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun hideProgress() {
        binding.updatebtn.show()
    }



//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.updatebtn -> {
//                val intentSignUp = Intent(this, HomeActivity::class.java)
//                startActivity(intentSignUp)
//            }
//        }
//    }

}