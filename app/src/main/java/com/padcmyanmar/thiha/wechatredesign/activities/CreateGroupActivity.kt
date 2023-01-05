package com.padcmyanmar.thiha.wechatredesign.activities

import VIEW_TYPE_SELECT
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.activities.BaseActivity.Companion.INTENT_TYPE_IMAGE
import com.padcmyanmar.thiha.wechatredesign.adapter.ContactGroupAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.CreateGroupPresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.CreateGroupPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.CreateGroupView
import com.padcmyanmar.thiha.wechatredesign.utils.afterTextChanged
import com.padcmyanmar.thiha.wechatredesign.utils.loadBitmapFromUri
import com.padcmyanmar.thiha.wechatredesign.utils.scaleToRatio
import com.padcmyanmar.thiha.wechatredesign.utils.toContactGroupList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_group.*

class CreateGroupActivity : BaseActivity() , CreateGroupView {
    lateinit var mPresenter: CreateGroupPresenter
    lateinit var mContactGroupAdapter: ContactGroupAdapter
    private var mChosenImageBitmap: Bitmap? = null
    private var allContacts: ArrayList<ContactVO> = arrayListOf()


    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, CreateGroupActivity::class.java)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        setUpPresenter()
        setUpRecyclerView()
        setUpListeners()
        mPresenter.onUiReady(this)
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[CreateGroupPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView() {
        mContactGroupAdapter = ContactGroupAdapter(VIEW_TYPE_SELECT, mPresenter, mPresenter)
        rvSelectContactGroup.adapter = mContactGroupAdapter
    }

    private fun setUpListeners(){
        ivGroupPhoto.setOnClickListener {
            mPresenter.pickGroupImage()
        }

        btnCreateGroup.setOnClickListener {
            if(edtGroupName.text.toString().isNotEmpty() && mChosenImageBitmap != null){
                mPresenter.createGroup(
                    name = edtGroupName.text.toString(),
                    bitmap = mChosenImageBitmap!!,
                )
            }
        }

        ivCreateGroupBack.setOnClickListener {
            mPresenter.onTapBack()
        }

        edtCreateGroupSearch.afterTextChanged {
            if (it.isEmpty()) {
                mContactGroupAdapter.setNewData(allContacts.toContactGroupList())
                layoutCreateGroupEmpty.visibility = View.GONE
            } else {

                val contactSearchList = allContacts.filter { contactVO ->
                    contactVO.name.lowercase().startsWith(it.lowercase())
                }.toContactGroupList()

                mContactGroupAdapter.setNewData(contactSearchList)


                if(contactSearchList.isEmpty()){
                    layoutCreateGroupEmpty.visibility = View.VISIBLE
                }else{
                    layoutCreateGroupEmpty.visibility = View.GONE
                }
            }
        }
    }

    override fun bindContacts(contactList: List<ContactVO>) {
        allContacts.clear()
        allContacts.addAll(contactList)
        mContactGroupAdapter.setNewData(contactList.toContactGroupList())
    }

    override fun pickGroupImage() {
        selectImageFromGallery(type = INTENT_TYPE_IMAGE)
    }

    override fun createGroupSuccess() {
        setResult(RESULT_OK)
        finish()
    }

    override fun navigateBack() {
        super.onBackPressed()
    }



    override fun showError(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
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
                        ImageViewCompat.setImageTintList(ivGroupPhoto, null)
                        ivGroupPhoto.setImageBitmap(it)
                    }

            }
        }
    }}