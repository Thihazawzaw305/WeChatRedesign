package com.padcmyanmar.thiha.wechatredesign.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.fragments.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var loadingDialog: Dialog
    companion object{
        fun newIntent(context : Context) : Intent{
            return Intent(context,HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpBottomNavWithFragment()
        setUpLoadingDialog()
    }

    private fun setUpLoadingDialog() {
        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.loading_dialog)
        loadingDialog.setCancelable(false)
    }
    private fun setUpBottomNavWithFragment(){
        switchFragment(MomentFragment())
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem : MenuItem ->
            when(menuItem.itemId){
                R.id.action_moment -> {

                    switchFragment(MomentFragment())
                    setResult(Activity.RESULT_OK)

                }
                R.id.action_Chat -> {

                    switchFragment(ChatFragment())
                    setResult(Activity.RESULT_OK)

                }
                R.id.action_Contacts -> {

                    switchFragment(ContactsFragment())
                    setResult(Activity.RESULT_OK)

                }
                R.id.action_Me -> {

                    switchFragment(MeFragment())
                    setResult(Activity.RESULT_OK)

                }

                else -> {

                    switchFragment(SettingFragment())
                    setResult(Activity.RESULT_OK)

                }

            }

            true
        }
    }

    private fun switchFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer,fragment)
            .commit()
    }
}