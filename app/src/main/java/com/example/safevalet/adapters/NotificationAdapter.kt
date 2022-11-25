package com.example.safevalet.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safevalet.databinding.CarItemsBinding
import com.example.safevalet.databinding.HistoryItemsBinding
import com.example.safevalet.databinding.NotificationItemBinding
import com.example.safevalet.model.NotificationDataResponse


class NotificationAdapter(private val list: ArrayList<ArrayList<NotificationDataResponse>>,
                          val context: Context): RecyclerView.Adapter<NotificationAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.binding.statTittle.text = list[position][0].bodyAR

        val date = list[position][0].date

        val time = list[position][0].time

        "$date \t\t $time".also { holder.binding.stationDate.text = it }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    class Holder(val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root)

}



