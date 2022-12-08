package com.example.safevalet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.safevalet.databinding.BookPlaceBinding
import com.example.safevalet.fragments.NotificationFragment
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
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
    private val userID by lazy { HelperUtils.getUID(applicationContext)}
    private lateinit var carId: String
    private lateinit var nickname: String
    private lateinit var carMake: String
    private lateinit var carModel: String
    private lateinit var year: String
    private lateinit var plateNo: String

//    private var flag = "0"
    private var jorFlag = "0"

    companion object {
        var flag = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BookPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        val user_id = sharedPreferences.getString("uid", null)
        val intent = Intent.getIntent("")


        binding.toolbarInclude.toolbar.title = resources.getString(R.string.car_registration)

        if (HelperUtils.getLang(applicationContext) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }

        binding.toolbarInclude.notficationBtn.setOnClickListener {
            HomeActivity.comeFromRegister = 1
            startActivity(Intent(applicationContext, HomeActivity::class.java))

        }
        binding.toolbarInclude.menuNotfication.setOnClickListener{
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }


        binding.checkBox1.setOnClickListener{
            binding.checkBox1.isChecked = true
            binding.checkBox2.isChecked = false
            jorFlag = "1"

        }

        binding.checkBox2.setOnClickListener{
            binding.checkBox2.isChecked = true
            binding.checkBox1.isChecked = false
            jorFlag = "0"
        }

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


//        flag = this@CarRegistration.intent.extras!!.getString("flag").toString()
        Log.i("Flag 00", "onCreate: $flag")



        if (flag != "0")
        {

            carId = this@CarRegistration.intent.extras!!.getString("carID").toString()
            nickname = this@CarRegistration.intent.extras!!.getString("nickname").toString()
            carMake = this@CarRegistration.intent.extras!!.getString("carMake").toString()
            carModel = this@CarRegistration.intent.extras!!.getString("carModel").toString()
            year = this@CarRegistration.intent.extras!!.getString("year").toString()
            plateNo = this@CarRegistration.intent.extras!!.getString("plateNo").toString()

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
                if (flag == "0") {
                    userVM.carRegister(
                        userID.toString(),
                        binding.nickname.text.toString(),
                        binding.plateNo.text.toString(),
                        binding.carMake.text.toString(),
                        binding.carModel.text.toString(),
                        binding.year.text.toString(),
                        language,
                        jorFlag.toString()
                    )
//                    Toast.makeText(applicationContext, "Car Registered", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, HomeActivity::class.java))

                } else {

                    userVM.updateCar(
                        carId.toString(),
                        language,
                        binding.nickname.text.trim().toString(),
                        binding.carMake.text.trim().toString(),
                        binding.carModel.text.trim().toString(),
                        binding.year.text.trim().toString(),
                        binding.plateNo.text.trim().toString()
                    )

                    Toast.makeText(applicationContext, "Updated", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
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
            putString("nickname", car.carNickName.toString())
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