package com.padcmyanmar.thiha.wechatredesign.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.WelcomePresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.WelcomePresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.WelcomeView
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() , WelcomeView {
    companion object{
        fun newIntent(context : Context) : Intent {
            return Intent(context,WelcomeActivity::class.java)
        }}
    // Presenter
    private lateinit var mPresenter: WelcomePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(FirebaseAuth.getInstance().currentUser !=null){
            navigateToMomentScreen()

        }
        setContentView(R.layout.activity_welcome)

        setUpPresenter()
        setUpListeners()
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[WelcomePresenterImpl::class.java]
        mPresenter.initView(this)
    }


    override fun navigateToLoginScreen() {
     startActivity(LoginActivity.newIntent(this))

    }

    override fun navigateToSignUpScreen() {
     startActivity(GetOTPActivity.newIntent(this))


    }

    override fun navigateToHomeScreen() {
   //  startActivity(HomeActivity.newIntent(this))
    }

    override fun showError(error: String) {
       Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
    }

    private fun setUpListeners(){
        btnSignUp.setOnClickListener {
            navigateToSignUpScreen()

        }

        btnLogin.setOnClickListener {
            navigateToLoginScreen()
        }
    }

    private fun navigateToMomentScreen(){
        startActivity(HomeActivity.newIntent(this))
    }

}