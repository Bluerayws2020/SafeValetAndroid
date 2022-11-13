package com.example.safevalet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.Navigation
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentHomeBinding



class HomeFragment: BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    private var navController: NavController? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.showMyQRImg.setOnClickListener(this)
        binding.callBackMyCarImg.setOnClickListener(this)
        binding.exchange.setOnClickListener(this)


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.showMyQRImg -> {
                Navigation.findNavController(v)
                    .navigate(R.id.action_homeFragment_to_showMyCarFragment)

            }

            R.id.callBackMyCarImg -> {
                Navigation.findNavController(v)
                    .navigate(R.id.action_homeFragment_to_callBackMyCarFragment)

            }

            R.id.exchange -> {
                Navigation.findNavController(v)
                    .navigate(R.id.action_homeFragment_to_exchangeCarFragment)
            }

        }
    }
}