package com.padcmyanmar.thiha.wechatredesign.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.data.vos.passwordVO
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.LogInPresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.LogInPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.LogInView
import com.padcmyanmar.thiha.wechatredesign.utils.afterTextChanged
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LogInView {
 private   lateinit var mPresenter : LogInPresenter
 var password = passwordVO()

    companion object{
        fun newIntent(context : Context) : Intent{
            return Intent(context,LoginActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpPresenter()
        setUpListeners()
        setUpPhoneAndPassword()

    }

    private fun setUpListeners(){
        btnLoginFromLoginScreen.setOnClickListener {
           if (checkButtonAvailable()){
               mPresenter.onTapLogIn(etPhoneNumber.text.toString(),etPassword.text.toString())

           }

        }

        btnBackFromLoginScreen.setOnClickListener {
            mPresenter.onTapBack()
        }
    }

    private fun setUpPhoneAndPassword() {
        etPhoneNumber.afterTextChanged { text ->
            if (text.isEmpty()) {
                etPhoneNumber.error = "Phone cannot be empty."
            }
            if (text.length < 6) {
                etPhoneNumber.error = "Phone need at least 6 characters."
            }
            checkButtonAvailable()
        }

        etPassword.afterTextChanged { text ->
            if (text.isEmpty()) {
                etPassword.error = "Password cannot be empty."
            }
            if (text.length < 6) {
                etPassword.error = "Password need at least 6 characters."
            }
            checkButtonAvailable()
        }
    }

    private fun validate(): Boolean {
        return etPhoneNumber.text?.isNotEmpty() == true &&
                etPhoneNumber.text!!.length > 5 &&
                etPassword.text?.isNotEmpty() == true &&
                etPassword.text!!.length > 5

    }

    private fun checkButtonAvailable(): Boolean {
        if (validate()) {
            btnLoginFromLoginScreen.alpha = 1F
            return true
        } else {
            btnLoginFromLoginScreen.alpha = 0.5f
            return false
        }
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[LogInPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    override fun navigateToMomentScreen() {
       startActivity(HomeActivity.newIntent(this))
        finish()
    }

    override fun navigateToWelcomeScreen() {
      onBackPressed()
    }



    override fun showError(error: String) {
        Snackbar.make(window.decorView, error, Snackbar.LENGTH_SHORT).show()
    }
}