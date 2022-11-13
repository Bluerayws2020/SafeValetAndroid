package com.example.safevalet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.databinding.FragmentCallBackMycarBinding
import com.example.safevalet.databinding.LanguageBinding


class LanguageFragment: BaseFragment<LanguageBinding>(), View.OnClickListener {


    private var navController: NavController? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LanguageBinding {
        return LanguageBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}
