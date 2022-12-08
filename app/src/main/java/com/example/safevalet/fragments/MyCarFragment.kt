package com.example.safevalet.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safevalet.CarRegistration
import com.example.safevalet.HomeActivity
import com.example.safevalet.R
import com.example.safevalet.adapters.CarAdapter
import com.example.safevalet.adapters.OnClickListener
import com.example.safevalet.databinding.MeetDriverBinding
import com.example.safevalet.databinding.MyCarsBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.model.MyCarsDataModel
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel


class MyCarFragment: AppCompatActivity() {

    private var carAdapter: CarAdapter? = null
    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private lateinit var myCarList: ArrayList<MyCarsDataModel>
    private val language = "ar"


    private lateinit var binding: MyCarsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyCarsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        binding.toolbarInclude.toolbar.title = resources.getString(R.string.my_car)

        if (HelperUtils.getLang(this) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }

        binding.toolbarInclude.notficationBtn.hide()
        binding.toolbarInclude.menuNotfication.setOnClickListener{

            onBackPressed()
        }



        userVM.getSetCarResponse().observe(this){ result ->
            when (result) {
                is NetworkResults.Success -> {
                    if(result.data.status.status == 1)
                    {
                        Toast.makeText(this, result.data.status.msg, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Failed Selected", Toast.LENGTH_SHORT).show()

                    }

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Log.d("=lol",result.exception.toString())
                }
            }

        }


        userVM.getMyCarResponse().observe(this) { result ->
            binding.swipeToRefresh.isRefreshing = false
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {

                        myCarList = (result.data.cars)

                        carAdapter = CarAdapter(myCarList, this
                        , object : OnClickListener {


                                override fun updateCar(pid: String, lang: String?, nickname:String?, carMake:String?
                                                   , carModel:String?, year:String?, plateNo:String?) {
                                userVM.updateCar(pid.toString(),
                                    lang.toString(), nickname.toString(), carMake.toString(),
                                    carModel.toString(), year.toString(), plateNo.toString())
                            }

                                override fun removeCar(pid: String) {
                                    userVM.removeCar(pid.toString())
                                }
                        })

                        val layoutManager = LinearLayoutManager(this)
                        binding.carItems.layoutManager = layoutManager
                        binding.carItems.adapter = carAdapter

                        Toast.makeText(this, result.data.status.msg, Toast.LENGTH_SHORT).show()

                    } else {
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Log.d("=lol",result.exception.toString())
                }
            }

        }

        userVM.showMyCars()


        binding.AddCarBtn.setOnClickListener{
            val i = Intent(this, CarRegistration::class.java)
            CarRegistration.flag = "0"
            startActivity(i)
        }


    }
}















//                        carAdapter?.submitList( result.data.cars)
//                        Log.i("size", "onViewCreated: " + result.data.cars.size)
//                        carAdapter?.notifyDataSetChanged()

//                        result.data.cars.iterator().forEach { it ->
//                            myCarList.add(MyCarsDataModel(it.id, it.title,
//                                it.nickName, it.carMake, it.carModel, it.palteNo, it.year, it.field_isdefualt))
//                        }

//                        binding.carItems.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))




//            binding.carItems.adapter = carAdapter

//        binding.carItems.addItemDecoration(
//            DividerItemDecoration(
//                requireContext(),
//                RecyclerView.VERTICAL
//            )
//        )
//
//        binding.carItems.adapter = carAdapter
//

//
//        binding.swipeToRefresh.setOnRefreshListener {
//            userVM.showMyCars()
//        }


//                        Log.i("nickname", "onViewCreated: "+ result.data.cars[0].id)
//                        Log.i("luj", "onViewCreated: ${myCarList.size}")
