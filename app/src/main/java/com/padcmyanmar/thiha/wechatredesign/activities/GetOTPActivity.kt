package com.padcmyanmar.thiha.wechatredesign.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.GetOTPPresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.GetOTPPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.GetOTPView
import com.padcmyanmar.thiha.wechatredesign.utils.afterTextChanged
import kotlinx.android.synthetic.main.activity_get_otp_activity.*
import kotlin.random.Random

class GetOTPActivity : AppCompatActivity(), GetOTPView {

    private val random = Random
    private lateinit var OTP : String
    private lateinit var mPresenter: GetOTPPresenter


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GetOTPActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_otp_activity)
        setUpPresenter()
        setUpListeners()
        setUpPhoneAndOtp()
    }


    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[GetOTPPresenterImpl::class.java]
        mPresenter.initView(this)

    }

    private fun setUpListeners() {
        tvResendCode.isEnabled = false
        btnGetOTP.setOnClickListener {
            val text = etPhoneNumberFromGetOTP.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, "Required Phone Number", Toast.LENGTH_LONG).show()
            } else{
                calculateOTP()
                Log.d("et", etPhoneNumberFromGetOTP.toString())
                Toast.makeText(this, "OTP is $OTP ", Toast.LENGTH_SHORT).show()
                pinOtp.setText(OTP)
//                OTPInput1.setText(OTP[0].toString())
//                OTPInput2.setText(OTP[1].toString())
//                OTPInput3.setText(OTP[2].toString())
//                OTPInput4.setText(OTP[3].toString())
                btnGetOTP.isEnabled = false
                tvResendCode.isEnabled = true
            }


            tvResendCode.setOnClickListener {
                if (!btnGetOTP.isEnabled){
                    calculateOTP()
                    Log.d("et", etPhoneNumberFromGetOTP.toString())
                    Toast.makeText(this, "OTP is $OTP ", Toast.LENGTH_LONG).show()
                    pinOtp.setText(OTP)
//                    OTPInput1.setText(OTP[0].toString())
//                    OTPInput2.setText(OTP[1].toString())
//                    OTPInput3.setText(OTP[2].toString())
//                    OTPInput4.setText(OTP[3].toString())

                }

            }

        }
        btnBackFromGetOTPScreen.setOnClickListener {
            mPresenter.onTapBack()
        }

        btnVerify.setOnClickListener {
            mPresenter.onTapVerify(etPhoneNumberFromGetOTP.text.toString())
        }
    }

    override fun navigateToSignUpScreen(phoneNumber: String) {
        if(checkButtonAvailable()) {
            val checkOTP = pinOtp.text.toString()
            if (checkOTP == OTP) {
                startActivity(SignUpActivity.newIntent(this, phoneNumber))
            } else
                Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_LONG).show()
        }
    }

    override fun navigateToWelcomeScreen() {
        onBackPressed()
    }

    override fun showError(error: String) {
        Snackbar.make(window.decorView,error,Snackbar.LENGTH_SHORT).show()
    }

    private fun calculateOTP(){
         OTP = String.format("%04d",random.nextInt(1000))
    }




    private fun setUpPhoneAndOtp(){
        etPhoneNumberFromGetOTP.afterTextChanged { text ->
            if (text.isEmpty()) {
                etPhoneNumberFromGetOTP.error = "Phone cannot be empty."
            }
            if (text.length < 6) {
                etPhoneNumberFromGetOTP.error = "Phone need at least 6 characters."
            }
            checkButtonAvailable()

        }
        pinOtp.afterTextChanged { text ->
            checkButtonAvailable()
        }
    }

    private fun validate(): Boolean {
        return pinOtp.text?.length == 4 &&
                etPhoneNumberFromGetOTP.text?.isNotEmpty() == true &&
                etPhoneNumberFromGetOTP.text!!.length > 5
    }

    private fun checkButtonAvailable(): Boolean {
        if (validate()) {
            btnVerify.alpha = 1F
            return true
        } else {
            btnVerify.alpha = 0.5f
            return false
        }
    }
}