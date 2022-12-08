package com.example.safevalet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.safevalet.R
import com.example.safevalet.databinding.CellMenuItemBinding
import com.example.safevalet.databinding.HeaderitemBinding
import com.example.safevalet.fragments.ProfileFragment
import com.example.safevalet.helpers.HelperUtils



class MenuAdapter(val context: Context, val name:ArrayList<String>,
                  private val image:ArrayList<Int>, private val listener: OnMenuListener): RecyclerView.Adapter<MenuAdapter.Holder>() {
    private var navController: NavController? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            CellMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)


    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.binding.textView.text = name[position]
        holder.binding.imageView.setImageResource(image[position])
//        navController = Navigation.findNavController(holder.itemView)

        if (HelperUtils.getLang(context) == "ar"){
            holder.binding.arrowAr.setImageResource(R.drawable.left)
        }else {
            holder.binding.arrowAr.setImageResource(R.drawable.rsz_righ)
        }


        holder.binding.linearView.setOnClickListener{
            listener.onEvent(position)
        }

    }

    override fun getItemCount(): Int {
        return name.size
    }


    class Holder(val binding: CellMenuItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

}