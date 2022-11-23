package com.example.safevalet.fragments

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safevalet.R
import com.example.safevalet.adapters.CarAdapter
import com.example.safevalet.adapters.NotificationAdapter
import com.example.safevalet.databinding.FragmentNotificationBinding
import com.example.safevalet.databinding.HistoryBinding
import com.example.safevalet.databinding.MyCarsBinding
import com.example.safevalet.databinding.NotificationItemBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.MyCarsDataModel
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.NotificationDataResponse
import com.example.safevalet.viewmodel.UserViewModel

class NotificationFragment: BaseFragment<FragmentNotificationBinding>() {

    private var carAdapter: CarAdapter? = null
    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private lateinit var myCarList: ArrayList<MyCarsDataModel>
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(applicationContext())}
    private var notificationAdapter: NotificationAdapter? = null

    override fun getViewBinding
                (inflater: LayoutInflater, container: ViewGroup?)
            : FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(inflater, container, false)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        notificationFunction

//        binding.toolbarInclude.toolbar.setBackgroundResource(R.drawable.home)
        binding.toolbarInclude.toolbar.title = resources.getString(R.string.Notification)

        if (HelperUtils.getLang(applicationContext()) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }

        binding.toolbarInclude.menuNotfication.setOnClickListener{
//            onBackPressedDispatcher.onBackPressed()
            navController?.navigate(R.id.homeFragment)
        }



        userVM.getNotificationLive()


//        binding.toolbarInclude.notficationBtn.setOnClickListener {

            userVM.getNotification().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.msg.status  == 1) {
                            Toast.makeText(applicationContext(), result.data.msg.msg, Toast.LENGTH_SHORT).show()
                            setupRecyclerView(result.data.notification_data)
                            binding.progressBarStation.hide()

                            if(result.data.notification_data.isEmpty()){
                                binding.noDatatxt.show()

                            }

                        }

                    }
                    is NetworkResults.Error -> {
                        result.exception.printStackTrace()
                        Log.d("ERROR", "notificationFunction: " + "aya")
                    }
                }
//            }

        }




        binding.swipeToRefresh.setOnRefreshListener {
            binding.notficaionItems.adapter = null
            binding.progressBarStation.show()
            binding.noDatatxt.hide()
            userVM.getNotificationLive()
        }

    }


    private fun notificationFunction(){



    }

    private fun setupRecyclerView(ride: ArrayList<ArrayList<NotificationDataResponse>>){

        notificationAdapter = NotificationAdapter(ride,applicationContext())
        val layoutManager = LinearLayoutManager(applicationContext())
        binding.notficaionItems.layoutManager = layoutManager
        binding.notficaionItems.adapter = notificationAdapter

    }


}