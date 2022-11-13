package com.example.safevalet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.databinding.MeetDriverBinding

class ShowMyCarFragment: BaseFragment<MeetDriverBinding>(), View.OnClickListener {


    private var navController: NavController? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MeetDriverBinding {
        return MeetDriverBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}