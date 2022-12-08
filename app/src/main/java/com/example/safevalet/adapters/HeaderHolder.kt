package com.example.safevalet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.safevalet.R
import java.lang.ref.WeakReference

class HeaderHolder (itemView:View): RecyclerView.ViewHolder(itemView){

private val view = WeakReference(itemView)
    private var name : TextView? =null
    private var phone : TextView? =null

    var  contatName:String = ""
    var  contatPhone:String = ""

    init {
    view.get().let {
        name = it?.findViewById(R.id.header_iv_profile)
        phone = it?.findViewById(R.id.phoneMobile)

    }
    }

    fun updateView(){
        name?.text = contatName
        phone?.text = contatPhone

    }
}