package com.example.safevalet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.size
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.safevalet.adapters.HistoryAdapter
import com.example.safevalet.adapters.MenuAdapter
import com.example.safevalet.adapters.NotificationAdapter
import com.example.safevalet.adapters.OnMenuListener
import com.example.safevalet.databinding.ActivityHomeBinding
import com.example.safevalet.fragments.LanguageFragment
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.show
import com.example.safevalet.model.NetworkResults
import com.example.safevalet.model.NotificationDataResponse
import com.example.safevalet.viewmodel.UserViewModel
import retrofit2.HttpException

class HomeActivity: AppCompatActivity() {  //, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val userVM by viewModels<UserViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(applicationContext)}
    private var notificationAdapter: NotificationAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        binding.layoutToolBar.show()




        userVM.getUserInfo().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                     binding.name.text = result.data.data.userName.toString()
                        binding.phoneMobile.text = result.data.data.phone.toString()

                            Glide.with(applicationContext)
                                .load(result.data.data.image)
                                .placeholder(R.drawable.ic_user_profile)
                                .error(R.drawable.ic_user_profile)
                                .into(binding.headerIvProfile)


                    } else {
                        Toast.makeText(applicationContext, result.data.status.msg, Toast.LENGTH_SHORT)
                            .show()
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





        val listMenuName = arrayListOf<String>("My Car","History", "Share", "My Profile", "Notifications", "Language", "Logout")
        val listMenuImage = arrayListOf<Int>(R.drawable.car, R.drawable.history, R.drawable.share,
            R.drawable.user, R.drawable.bluebell, R.drawable.translating, R.drawable.logout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController!!.graph)


        Toast.makeText(this@HomeActivity, userID, Toast.LENGTH_SHORT).show()


        binding.toolbarInclude.menuNotfication.setOnClickListener {
            binding.menuRecycler.show()
        }

        binding.toolbarInclude.homeIcon.show()
//        binding.toolbarInclude.toolbar.background = R.drawable.home

        val adapter = MenuAdapter(this, listMenuName, listMenuImage,object : OnMenuListener{
            override fun onEvent(position: Int) {
                when (position) {
                    0 -> {
                        navController?.navigate(R.id.myCarFragment)
            //                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                        binding.menuRecycler.hide()

                    }
                    1 -> {
                        navController?.navigate(R.id.historyFragment)
            //                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                        binding.menuRecycler.hide()

                    }
                    2 -> {
                        Toast.makeText(this@HomeActivity, "Share", Toast.LENGTH_SHORT).show()

                        val intent= Intent()
                        intent.action=Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.blueray.valet_driver")
                        intent.type="text/plain"
                        startActivity(Intent.createChooser(intent,"Share To:"))

            //                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                        binding.menuRecycler.hide()
//                        binding.layoutToolBar.hide()

                    }
                    3 -> {
                        navController?.navigate(R.id.profileFragment)
            //                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                        binding.menuRecycler.hide()
//                        binding.layoutToolBar.hide()

                    }
                    4 -> {
                        // Settings -> Notifications
            //                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                        navController?.navigate(R.id.notificationFragment)
                        binding.menuRecycler.hide()
//                        binding.layoutToolBar.hide()

                    }
                    5 -> {

                        startActivity(Intent(this@HomeActivity,LanguageFragment::class.java))
                        binding.menuRecycler.hide()
//                        binding.layoutToolBar.hide()

                    }
                    6 -> {
                        logout()
                        userVM.logoutUser(userID)

                    }
                }
            }

        })
//        binding.layoutToolBar.show()

        binding.navigationView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.navigationView.layoutManager = layoutManager




    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController!!, appBarConfiguration)

    }

    private fun logout(){

        userVM.getCustomerLogoutResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        logoutFunction()
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
                else -> {}
            }

        }

    }



    private fun logoutFunction() {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        val intentSplash = Intent(this, MainActivity::class.java)
        startActivity(intentSplash)
        finish()
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

