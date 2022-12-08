package com.example.safevalet.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.HomeActivity
import com.example.safevalet.MainActivity
import com.example.safevalet.R
import com.example.safevalet.databinding.FragmentCallBackMycarBinding
import com.example.safevalet.databinding.LanguageBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide


class LanguageFragment: AppCompatActivity() {

    private lateinit var binding: LanguageBinding

//    val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        navController = Navigation.findNavController(view)


        binding.toolbarInclude.toolbar.title = resources.getString(R.string.language)
        binding.toolbarInclude.toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))
        binding.toolbarInclude.fullToolBar.setBackgroundColor(Color.parseColor("#2596be"))

        if (HelperUtils.getLang(this) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }

        binding.toolbarInclude.menuNotfication.setOnClickListener{
//            onBackPressedDispatcher.onBackPressed()
//            startActivity(Intent(this, HomeActivity::class.java))
            onBackPressed()
        }

        binding.toolbarInclude.notficationBtn.setOnClickListener {
//            startActivity(Intent(applicationContext, NotificationFragment::class.java))
        }



        binding.arabicBtn.setOnClickListener {
            val language = "ar"

            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

            sharedPreferences.edit()?.putString("lang", language)?.apply()
            val intentSplash = Intent(this, MainActivity::class.java)
            startActivity(intentSplash)

        }
        binding.englishBtn.setOnClickListener {

            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

            val language = "en"


            sharedPreferences?.edit()?.putString("lang", language)?.apply()
            val intentSplash = Intent(this, MainActivity::class.java)
            startActivity(intentSplash)


        }
    }
}
