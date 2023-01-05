package com.padcmyanmar.thiha.wechatredesign.fragments

import PICK_IMAGE_REQUEST
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.adapter.MomentRecyclerViewAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import com.padcmyanmar.thiha.wechatredesign.fragments.EditProfileFragment.Companion.selectedDay
import com.padcmyanmar.thiha.wechatredesign.fragments.EditProfileFragment.Companion.selectedGender
import com.padcmyanmar.thiha.wechatredesign.fragments.EditProfileFragment.Companion.selectedMonth
import com.padcmyanmar.thiha.wechatredesign.fragments.EditProfileFragment.Companion.selectedName
import com.padcmyanmar.thiha.wechatredesign.fragments.EditProfileFragment.Companion.selectedPhone
import com.padcmyanmar.thiha.wechatredesign.fragments.EditProfileFragment.Companion.selectedYear
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.MePresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.MePresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.MeView
import com.padcmyanmar.thiha.wechatredesign.network.CloudFirestoreFirebaseApiImpl
import com.padcmyanmar.thiha.wechatredesign.network.FirebaseApi
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.qr_dialog.*
import kotlinx.coroutines.delay
import java.io.IOException

class MeFragment : Fragment() ,MeView{
    lateinit var mPresenter : MePresenter
    private val userVO  =UserVO()
    private lateinit var mMomentAdapter : MomentRecyclerViewAdapter
    lateinit var qrDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresenter()
        setUpRecyclerview()
        setUpListeners()
        mPresenter.onUiReady(this)
    }

    private fun setUpRecyclerview(){
        mMomentAdapter = MomentRecyclerViewAdapter(mPresenter)
        rvBookmarkedMoments.adapter = mMomentAdapter
        rvBookmarkedMoments.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[MePresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListeners(){
//        btnUpload.setOnClickListener {
//        mPresenter.onTapUpload(userVO)
//        }

        ivEditProfile.setOnClickListener {
            mPresenter.onTapEdit()
        }

        ivQr.setOnClickListener {
            mPresenter.onTapQr()
        }
    }

    override fun bindUserData(

        name: String,
        phoneNumber: String,
        gender: String,
        dateOfBirth: String,
        profileImage: String
    ) {

      tvUserNameFromMeFragment.text = name
        tvPhoneNumber.text = phoneNumber
        tvDateOfBirth.text = dateOfBirth
        tvGender.text = gender
        Glide.with(this)
            .load(profileImage)
            .into(ivProfileImage)
    }


//    override fun openGallery() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
//    }

    override fun bindMoments(momentList: List<MomentVO>) {
        mMomentAdapter.setNewData(momentList)
    }

    override fun showEditDialog(name: String, phone: String, dob: String, gender: String) {
        selectedName = name
        selectedPhone = phone
        selectedGender = gender
        val dobList = dob.split("/")
        if (dobList.size == 3) {
            selectedDay = dobList[0]
            selectedMonth = dobList[1]
            selectedYear = dobList[2]

            val dialogFragment = EditProfileFragment()
            dialogFragment.setDelegate(mPresenter)
            dialogFragment.show(
                childFragmentManager, EditProfileFragment.TAG
            )

        }
    }

    override fun showQrDialog(qrCode: String) {
        qrDialog = Dialog(requireContext())
        qrDialog.setContentView(R.layout.qr_dialog)
        qrDialog.setCancelable(true)
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(qrCode, BarcodeFormat.QR_CODE, 250, 250)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            qrDialog.ivDialogQr.setImageBitmap(bitmap)
            qrDialog.btnQrClose.setOnClickListener {
                qrDialog.dismiss()
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        qrDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        qrDialog.show()
    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
//            if (data == null || data.data == null) {
//                return
//            }
//
//            val filePath = data.data
//            try {
//
//                filePath?.let {
//                    if (Build.VERSION.SDK_INT >= 29) {
//                        val source : ImageDecoder.Source =
//                            ImageDecoder.createSource(this.requireContext().contentResolver, filePath)
//
//                        val bitmap = ImageDecoder.decodeBitmap(source)
//                        mPresenter.onPhotoTaken(bitmap)
//                    } else {
//                        val bitmap = MediaStore.Images.Media.getBitmap(
//                            requireContext().contentResolver, filePath
//                        )
//                        mPresenter.onPhotoTaken(bitmap)
//                    }
//                }
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
 //  }

    override fun showError(error: String) {

    }


}

