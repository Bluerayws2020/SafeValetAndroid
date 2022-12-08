package com.example.safevalet

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.safevalet.databinding.ActivityMainBinding
import com.example.safevalet.databinding.SplashScreenBinding
import com.example.safevalet.helpers.ContextWrapper
import com.example.safevalet.helpers.HelperUtils
import java.util.*

class MainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

//    private val callback = object :OnBackPressedCallback(false){
//        override fun handleOnBackPressed() {
//            println("HELLO")
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.hide()


//        onBackPressedDispatcher.addCallback(this, callback)
//        callback.isEnabled = true


//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            if (HelperUtils.getUID(this) != "0") {
                openHome()
            }
            else
                openLogin()
        }, 2000)
//        openLogin()




    }

    private fun openHome() {
        val intentHome = Intent(this, HomeActivity::class.java)
        startActivity(intentHome)
        finish()
    }


    private fun openLogin() {
        val intentHome = Intent(this, LoginActivity::class.java)
        startActivity(intentHome)
        finish()
    }


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }

}