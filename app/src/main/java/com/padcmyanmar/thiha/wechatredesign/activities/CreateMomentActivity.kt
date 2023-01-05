package com.padcmyanmar.thiha.wechatredesign.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.adapter.FileAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.FileVO
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.CreateMomentPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.CreateMomentView
import com.padcmyanmar.thiha.wechatredesign.utils.getRealPathFromURI
import com.padcmyanmar.thiha.wechatredesign.utils.loadBitmapFromUri
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_moment.*

class CreateMomentActivity :BaseActivity(),CreateMomentView {
    private lateinit var mPresenter: CreateMomentPresenterImpl
    private lateinit var mFileAdapter: FileAdapter
    private var selectedFileList: ArrayList<FileVO> = arrayListOf()
    private lateinit var loadingDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_moment)
        setUpPresenter()
        setUpLoadingDialog()
        setUpListeners()
        setUpFileRecyclerView()

        mPresenter.onUiReady(this)
    }

    private fun setUpLoadingDialog() {
        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.loading_dialog)
        loadingDialog.setCancelable(false)
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[CreateMomentPresenterImpl::class.java]
        mPresenter.initView(this)

    }

    private fun setUpFileRecyclerView() {
        mFileAdapter = FileAdapter(mPresenter)
        rvFile.adapter = mFileAdapter
        rvFile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpListeners() {
        ivPickFile.setOnClickListener {
            mPresenter.onTapPickFile()
        }
        btnConfirmToCreateMoment.setOnClickListener {
            mPresenter.uploadMoment(etCreateMoment.text.toString(), selectedFileList)
        }

        btnDismissMoment.setOnClickListener {
            mPresenter.onTapBack()
        }

    }

    override fun navigateBack() {
        onBackPressed()
    }

    override fun pickFiles() {
        selectImageFromGallery(INTENT_TYPE_FILE)
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onBindUserData(name: String, profileImage: String) {
        Glide.with(this)
            .load(profileImage)
            .into(ivProfileImageFromCreateMoment)

        tvUserNameFromCreateMoment.text = name
    }

    override fun showLoadingDialog() {
        loadingDialog.show()
    }

    override fun dismissLoadingDialog() {
        setResult(Activity.RESULT_OK)
        loadingDialog.dismiss()
        mPresenter.onTapBack()
    }

    override fun onFileRemove(fileVO: FileVO) {
      selectedFileList.remove(fileVO)
        mFileAdapter.setNewData(selectedFileList)
    }

    override fun showError(error: String) {
        Snackbar.make(window.decorView, error, Snackbar.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY) {
            val clipPath = data?.clipData


            if (clipPath != null) {

                val uriList: ArrayList<Uri> = arrayListOf()
                Log.d("image_count", clipPath.itemCount.toString())
                for (i in 0 until clipPath.itemCount) {
                    if (clipPath.getItemAt(i).uri.toString().contains("video")) {
                        Log.d("image_invalid", "invalid file")
                        continue
                    } else if (clipPath.getItemAt(i).uri.toString().contains("image")) {
                        uriList.add(clipPath.getItemAt(i).uri)
                    }
                }
                Observable.just(uriList)
                    .map {
                        uriList.map { uri ->
                            Pair<Uri, Bitmap>(uri, uri.loadBitmapFromUri(this))
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("image_count_bitmap", it.size.toString())

                        selectedFileList.clear()
                        it.forEach { pair ->
                            selectedFileList.add(
                                FileVO(
                                    uri = pair.first,
                                    bitmap = pair.second,
                                    isMovie = pair.first.toString().contains("video")
                                )
                            )
                        }

//                        mBitMapList = it
//                        mPhotoAdapter.setNewData(it)

                        mFileAdapter.setNewData(selectedFileList)
                    }
            } else {
                val singleImage = data?.data

                if (singleImage != null) {
                    Observable.just(singleImage)
                        .map { Pair<Uri, Bitmap>(it,it.loadBitmapFromUri(this)) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            selectedFileList.clear()
                            if(it.first.toString().contains("video")){
                                val realPath = getRealPathFromURI(this,it.first).toString()
                                selectedFileList.add(
                                    FileVO(uri = it.first, bitmap = it.second, isMovie = it.first.toString().contains("video"), realPath = realPath)
                                )
                            }else{
                                selectedFileList.add(
                                    FileVO(uri = it.first, bitmap = it.second, isMovie = it.first.toString().contains("video"))
                                )
                            }

//                            mPhotoAdapter.setNewData(listOf(it))
                            mFileAdapter.setNewData(selectedFileList)

                        }
                }
            }
        }
    }
}