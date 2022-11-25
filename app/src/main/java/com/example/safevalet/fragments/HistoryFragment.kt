package com.example.safevalet.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safevalet.R
import com.example.safevalet.adapters.CarAdapter
import com.example.safevalet.adapters.HistoryAdapter
import com.example.safevalet.databinding.HistoryBinding
import com.example.safevalet.databinding.MyCarsBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.HistoryData
import com.example.safevalet.model.HistoryRidesModel
import com.example.safevalet.model.MyCarsDataModel
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel


class HistoryFragment: BaseFragment<HistoryBinding>(), AdapterView.OnItemSelectedListener,  View.OnClickListener {

    private var historyAdapter: HistoryAdapter? = null
    private var navController: NavController? = null
    private val userVM by viewModels<UserViewModel>()
    private var myHistoryList: List<HistoryData>? = null
    private val language = "ar"

    override fun getViewBinding
                (inflater: LayoutInflater, container: ViewGroup?)
            : HistoryBinding {
        return HistoryBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)



        binding.toolbarInclude.toolbar.title = resources.getString(R.string.history)

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


        binding.swipeToRefresh.setOnRefreshListener {
            binding.historyItems.adapter = null
            binding.progressBarStation.show()
            binding.noDatatxt.hide()
            userVM.getNotificationLive()
        }


        userVM.getUserHistoryResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {

                        Log.i("ruby", "onViewCreated: " + result.data.status.msg)

                        myHistoryList = result.data.data

                        historyAdapter = HistoryAdapter(myHistoryList!!, requireContext())

                        val layoutManager = LinearLayoutManager(context)
                        binding.historyItems.layoutManager = layoutManager
                        binding.historyItems.adapter = historyAdapter
                        binding.progressBarStation.hide()


                        Toast.makeText(requireContext(), result.data.status.msg, Toast.LENGTH_SHORT).show()

                    } else {
                        toast("Empty")
                        binding.progressBarStation.hide()
                        binding.noDatatxt.show()

                    }
                    Log.d("=lol",result.toString())

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Log.d("=lol",result.exception.toString())
                    toast("ERROR")
                }
            }
        }


        binding.historyItems.adapter = historyAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}