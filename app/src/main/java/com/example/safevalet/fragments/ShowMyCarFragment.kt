package com.example.safevalet.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.safevalet.R
import com.example.safevalet.databinding.MeetDriverBinding
import com.example.safevalet.helpers.HelperUtils
import com.example.safevalet.helpers.ViewUtils.hide
import com.example.safevalet.helpers.ViewUtils.show
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class ShowMyCarFragment: BaseFragment<MeetDriverBinding>(), View.OnClickListener {


    private var navController: NavController? = null
    private val userID by lazy { HelperUtils.getUID(applicationContext())}


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MeetDriverBinding {
        return MeetDriverBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        navController = Navigation.findNavController(view)
        val sharedPreferences = mContext?.getSharedPreferences(HelperUtils.SHARED_PREF, Context.MODE_PRIVATE)


        binding.toolbarInclude.toolbar.title = resources.getString(R.string.show_my_qr)


        val nickName = sharedPreferences?.getString("nickname", "")
        val plateNo = sharedPreferences?.getString("plateNo", "")

        if(nickName != null || plateNo != null) {
            "$nickName\t  - \t$plateNo".also { binding.whiteMyCar.text = it }
        }
        else if (nickName == null && plateNo == null){
            "My_Car".also { binding.whiteMyCar.text = it }
        }

        binding.toolbarInclude.notficationBtn.setOnClickListener {
            navController?.navigate(R.id.notificationFragment)
        }
//        binding.toolbarInclude.homeIcon.hide()

        //initializing MultiFormatWriter for QR code
        val mWriter = MultiFormatWriter()
        try {
            //BitMatrix class to encode entered text and set Width & Height
            val mMatrix =
                mWriter.encode(userID, BarcodeFormat.QR_CODE, 400, 400)
            val mEncoder = BarcodeEncoder()
            val mBitmap =
                mEncoder.createBitmap(mMatrix) //creating bitmap of code
            binding.QRImg.setImageBitmap(mBitmap) //Setting generated QR code to imageView

        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.QRImg -> {
            }


        }
    }
}