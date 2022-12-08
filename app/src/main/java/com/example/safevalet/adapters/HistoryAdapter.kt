package com.example.safevalet.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.safevalet.databinding.CarItemsBinding
import com.example.safevalet.databinding.HistoryItemsBinding
import com.example.safevalet.databinding.NotificationItemBinding
import com.example.safevalet.model.HistoryData
import com.example.safevalet.model.HistoryRidesModel
import com.example.safevalet.model.MyCarsDataModel



class HistoryAdapter(private val list: List<HistoryData>, val context: Context) :
        ListAdapter<HistoryData, HistoryAdapter.Holder>(DiffUtilCallback) {
        private var mContext: Context? = null

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            mContext = recyclerView.context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

            val binding =
                HistoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(binding)

        }

        override fun onBindViewHolder(holder: Holder, position: Int) {

            holder.binding.clientId.text = list[position].driver1
        }

        override fun getItemCount(): Int {
            return list.size
        }

        object DiffUtilCallback : DiffUtil.ItemCallback<HistoryData>() {
            override fun areItemsTheSame(
                oldItem: HistoryData,
                newItem: HistoryData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryData,
                newItem: HistoryData
            ): Boolean {
                TODO("Not yet implemented")
            }

        }

        class Holder(val binding: HistoryItemsBinding) : RecyclerView.ViewHolder(binding.root)


}