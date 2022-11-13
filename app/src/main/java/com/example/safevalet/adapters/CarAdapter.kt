package com.example.safevalet.adapters

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.safevalet.CarRegistration
import com.example.safevalet.databinding.CarItemsBinding
import com.example.safevalet.model.MyCarsDataModel


class CarAdapter(private val list: List<MyCarsDataModel>, val context: Context,
                 private val onCarListener: OnClickListener):
    ListAdapter<MyCarsDataModel, CarAdapter.Holder>(DiffUtilCallback) {
    private var mContext: Context? = null
    private val language = "ar"
//    private val userVM by viewModels<UserViewModel>()


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            CarItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val currentList = list[position]

        holder.binding.whiteMyCar.text = currentList.nickName
        holder.binding.carModel.text = currentList.palteNo


        holder.binding.deleteCar.setOnClickListener{
            onCarListener.removeCar(currentList.id.toString())
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        }


        holder.binding.editCar.setOnClickListener{

            val myActivity = Intent(context.applicationContext, CarRegistration::class.java)

            myActivity.addFlags(FLAG_ACTIVITY_NEW_TASK)
            myActivity.putExtra("flag", "1")
            myActivity.putExtra("carID", currentList.id)
            myActivity.putExtra("nickname", currentList.nickName)
            myActivity.putExtra("carMake", currentList.carMake)
            myActivity.putExtra("carModel", currentList.carModel)
            myActivity.putExtra("year", currentList.year)
            myActivity.putExtra("plateNo", currentList.palteNo)


            context.applicationContext.startActivity(myActivity)


            onCarListener.updateCar(currentList.id.toString(), language, currentList.nickName,
            currentList.carMake, currentList.carModel, currentList.year, currentList.palteNo)
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    object DiffUtilCallback : DiffUtil.ItemCallback<MyCarsDataModel>() {
        override fun areItemsTheSame(oldItem: MyCarsDataModel, newItem: MyCarsDataModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MyCarsDataModel,
            newItem: MyCarsDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    class Holder(val binding: CarItemsBinding) : RecyclerView.ViewHolder(binding.root)

}