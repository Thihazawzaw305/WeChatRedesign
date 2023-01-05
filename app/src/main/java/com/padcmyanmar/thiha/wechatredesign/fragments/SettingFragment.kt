package com.padcmyanmar.thiha.wechatredesign.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.activities.WelcomeActivity
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.SettingPresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.SettingPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.SettingView
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment(), SettingView {

    lateinit var mPresenter: SettingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresenter()
        setUpListener()

    }


    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[SettingPresenterImpl::class.java]
        mPresenter.initView(this)
    }


    private fun setUpListener() {
        btnLogout.setOnClickListener {
            mPresenter.onTapLogout()
        }
    }

    override fun navigateToWelcomeScreen() {
        startActivity(WelcomeActivity.newIntent(requireContext()))
        requireActivity().finish()

    }

    @SuppressLint("ResourceAsColor")
    override fun showConfrimDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Do you want to log out?")
            .setPositiveButton("OK") { _, _ ->
                FirebaseAuth.getInstance().signOut()
              navigateToWelcomeScreen()

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(Color.RED)
            val cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            cancelButton.setTextColor(R.color.text_color_primary)
        }
        dialog.show()
    }

    override fun showError(error: String) {

    }


}