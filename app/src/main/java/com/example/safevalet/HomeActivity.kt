package com.example.safevalet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.safevalet.databinding.ActivityHomeBinding
import com.example.safevalet.fragments.HistoryFragment
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeActivity: AppCompatActivity(), View.OnClickListener , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private lateinit var user_id: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController!!.graph)

        NavigationUI.setupActionBarWithNavController(this, navController!!, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navigationView, navController!!)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        user_id = sharedPreferences.getString("uid", null).toString()

    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {


            R.id.myCar -> {
                navController?.navigate(R.id.myCarFragment)
                return true
            }

            R.id.history -> {
                navController?.navigate(R.id.historyFragment)
                return true
            }

            R.id.share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.myProfile -> {
                navController?.navigate(R.id.profileFragment)
                return true
            }
            R.id.language -> {
                navController?.navigate(R.id.languageFragment)
                return true
            }
            else -> {
                return false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController!!, appBarConfiguration)

    }

}

