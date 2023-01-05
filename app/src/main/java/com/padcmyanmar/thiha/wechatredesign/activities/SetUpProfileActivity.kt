package com.padcmyanmar.thiha.wechatredesign.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.fragments.MomentFragment
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.SetUpProfilePresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.SetUpProfilePresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.SetUpProfileView
import com.padcmyanmar.thiha.wechatredesign.utils.loadBitmapFromUri
import com.padcmyanmar.thiha.wechatredesign.utils.scaleToRatio
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_set_up_profile.*

class SetUpProfileActivity : BaseActivity(), SetUpProfileView {

    lateinit var mPresenter: SetUpProfilePresenter
    var mChosenImageBitmap: Bitmap? = null


    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, SetUpProfileActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up_profile)
        setUpPresenter()
        setUpListener()
        mPresenter.onUiReady(this)
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[SetUpProfilePresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListener() {
        btnUploadProfile.setOnClickListener {
            mPresenter.uploadPicture(mChosenImageBitmap)
        }

        ivProfile.setOnClickListener {
            mPresenter.onTapPicture()
        }

        ivAddProfile.setOnClickListener {
            mPresenter.onTapPicture()
        }
    }

    override fun navigateToMomentScreen() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finishAffinity()
    }

    override fun showError(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun pickImageFromGallery() {
        selectImageFromGallery(INTENT_TYPE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == BaseActivity.INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY) {
            val imageUri = data?.data
            imageUri?.let { image ->

                Observable.just(image)
                    .map { it.loadBitmapFromUri(applicationContext) }
                    .map { it.scaleToRatio(0.35) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mChosenImageBitmap = it
                        ivProfile.setImageBitmap(it)
                    }

            }
        }
    }
}