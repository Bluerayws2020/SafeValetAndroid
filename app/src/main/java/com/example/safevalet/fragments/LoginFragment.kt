package com.example.safevalet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.R
import com.example.safevalet.databinding.ActivitySignupBinding
import com.example.safevalet.databinding.FragmentHomeBinding

class LoginFragment : BaseFragment<ActivitySignupBinding>(), View.OnClickListener {

    private var navController: NavController? = null


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        navController = Navigation.findNavController(view)
        binding.signUpBtn.setOnClickListener(this)

    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.signUpBtn -> {
                // booking place which means CarRegistration Activity
            }
        }
    }
}
