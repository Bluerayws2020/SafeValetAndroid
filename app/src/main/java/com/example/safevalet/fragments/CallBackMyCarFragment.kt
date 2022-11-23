package com.example.safevalet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.databinding.FragmentCallBackMycarBinding


class CallBackMyCarFragment: BaseFragment<FragmentCallBackMycarBinding>(), View.OnClickListener {


    private var navController: NavController? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCallBackMycarBinding {
        return FragmentCallBackMycarBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        navController = Navigation.findNavController(view)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}