package com.example.safevalet.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safevalet.databinding.CarItemsBinding
import com.example.safevalet.databinding.HistoryItemsBinding
import com.example.safevalet.databinding.NotificationItemBinding


class NotificationAdapter(val context: Context): RecyclerView.Adapter<NotificationAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

//        holder.binding.notificationTxt.text = list[position].name
    }

    override fun getItemCount(): Int {
        return 3
    }


    class Holder(val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root)

}