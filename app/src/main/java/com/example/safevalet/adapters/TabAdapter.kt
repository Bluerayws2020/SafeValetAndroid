package com.example.safevalet.adapters

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.safevalet.fragments.HomeFragment
import com.example.safevalet.fragments.LoginFragment
import java.security.AccessController.getContext

internal class TabAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int, val view: View): FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                LoginFragment()
            }

            1 -> {
                HomeFragment()
            }

            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }


}