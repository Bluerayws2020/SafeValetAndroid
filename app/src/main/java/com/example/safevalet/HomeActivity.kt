package com.example.safevalet

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safevalet.adapters.MenuAdapter
import com.example.safevalet.adapters.OnMenuListener
import com.example.safevalet.databinding.ActivityHomeBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.viewmodel.UserViewModel
import com.google.android.material.navigation.NavigationView
import retrofit2.HttpException

class HomeActivity: AppCompatActivity() {  //, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
//    private lateinit var user_id: String
    private val userID by lazy { HelperUtils.getUID(applicationContext)}




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val listMenuName = arrayListOf<String>("My Car","History", "Share", "My Profile", "Settings", "Language", "Logout")
        val listMenuImage= arrayListOf<Int>(R.drawable.car, R.drawable.history, R.drawable.share,
            R.drawable.user, R.drawable.settings, R.drawable.translating, R.drawable.logout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController!!.graph)



        val adapter = MenuAdapter(this, listMenuName, listMenuImage,object : OnMenuListener{
            override fun onEvent(position: Int) {
                if(position == 0){
                    navController?.navigate(R.id.myCarFragment)
                }
                else if (position == 1){
                    navController?.navigate(R.id.historyFragment)
                }
                else if(position == 2){
                    Toast.makeText(this@HomeActivity, "Share", Toast.LENGTH_SHORT).show()
                }
                else if(position == 3){
                    navController?.navigate(R.id.profileFragment)
                }
                else if(position == 4){
                    // Settings -> Notifications
                }
                else if(position == 5){
                    navController?.navigate(R.id.languageFragment)
                }
                else if(position == 6)
                {

                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    logout()
                    userVM.logoutUser(userID)

                }
            }

        })

        binding.navigationView.adapter = adapter

//        val mDrawerLayout = findViewById<RecyclerView>(binding.navigationView.id) as DrawerLayout
//        mDrawerLayout.closeDrawer(GravityCompat.END, false)

        val layoutManager = LinearLayoutManager(this)
        binding.navigationView.layoutManager = layoutManager


//
//        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//        user_id = sharedPreferences.getString("uid", null).toString()

        Log.i("id", "username: $userID")

    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController!!, appBarConfiguration)

    }

    private fun logout(){

        userVM.getCustomerLogoutResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        Toast.makeText(this@HomeActivity, result.data.status.msg, Toast.LENGTH_SHORT).show()
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        Log.e("TAG", "onViewCreated: ${result.exception}")
                    else
                        Log.e("TAG", "onViewCreated: ERROR")
                }
            }

        }

    }
}



//override fun onClick(v: View?) {
//    when (v?.id) {
//
//    }
//}
//
//
//override fun onNavigationItemSelected(item: MenuItem): Boolean {
//    binding.drawerLayout.closeDrawer(GravityCompat.START)
//    when (item.itemId) {
//
//
//        R.id.myCar -> {
//            navController?.navigate(R.id.myCarFragment)
//            return true
//        }
//
//        R.id.history -> {
//            navController?.navigate(R.id.historyFragment)
//            return true
//        }
//
//        R.id.share -> {
//            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
//            return true
//        }
//
//        R.id.myProfile -> {
//            navController?.navigate(R.id.profileFragment)
//            return true
//        }
//        R.id.language -> {
//            navController?.navigate(R.id.languageFragment)
//            return true
//        }
//        else -> {
//            return false
//        }
//    }
//}

