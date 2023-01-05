package com.padcmyanmar.thiha.wechatredesign.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.AccessControlProfile
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.SignUpPresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.SignUpPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.SignUpView
import com.padcmyanmar.thiha.wechatredesign.utils.afterTextChanged
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity(), SignUpView {
    private lateinit var loadingDialog: Dialog
    private lateinit var mPresenter: SignUpPresenter
    private val profileUrl: String = ""
    private var selectedDay: String = "1"
    private var selectedMonth: String = "1"
    private var selectedYear: String = "1995"
    private var selectedGender: String = ""
    private var checkedTermAndServices: Boolean = false

    companion object {
        const val PHONE_NUMBER = "PHONE_NUMBER"
        fun newIntent(context: Context, phoneNumber: String): Intent {
            val intent = Intent(context, SignUpActivity::class.java)
            intent.putExtra(PHONE_NUMBER, phoneNumber)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setUpPresenter()
        setUpEditText()
        setUpDateOfBirth()
        selectGender()
        setUpTermAndCondition()
        setUpLoadingDialog()
        setUpListeners()
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[SignUpPresenterImpl::class.java]
        mPresenter.initView(this)
    }


    private fun setUpListeners() {
        val phoneNumber = intent.getStringExtra(PHONE_NUMBER)

        btnSignUpFromSignUpScreen.setOnClickListener {

            if (checkButtonAvailable()) {
                if (phoneNumber != null) {
                    mPresenter.onTapSignUp(
                        phoneNumber,
                        etPasswordFromSignUp.text.toString(),
                        etName.text.toString(),
                        dateOfBirth = selectedDay.plus("/").plus(selectedMonth).plus("/").plus(selectedYear),
                        selectedGender,
                        profileUrl


                    )
                }
            }
        }



    }
    private fun setUpLoadingDialog() {
        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.loading_dialog)
        loadingDialog.setCancelable(false)
    }

    override fun navigateToSetUpProfile() {
        startActivity(SetUpProfileActivity.getIntent(this))
        finish()
    }



    override fun navigateToGetOTPScreen() {
        onBackPressed()
    }

    override fun showLoadingDialog() {
       loadingDialog.show()
    }

    override fun dismissLoadingDialog() {
        setResult(Activity.RESULT_OK)
        loadingDialog.dismiss()
    }


    override fun showError(error: String) {
        Snackbar.make(window.decorView, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun selectGender() {
        radioButtonGroupForGender.setOnCheckedChangeListener { group, selectedRadioId ->
            when (selectedRadioId) {


                R.id.rBtnMale -> {
                    selectedGender = rBtnMale.text.toString()


                }
                R.id.rBtnFemale -> {
                    selectedGender = rBtnFemale.text.toString()


                }
                R.id.rBtnOther -> {
                    selectedGender = rBtnOther.text.toString()

                }
            }
            checkButtonAvailable()
        }

    }

    private fun setUpDateOfBirth() {
        val dayArray = IntArray(30) { it + 1 }
        val dayAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            dayArray.toTypedArray()
        )
        spDay.adapter = dayAdapter
        spDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
                Log.d("day", parent?.getItemAtPosition(position).toString())
                selectedDay = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }


        val monthArray = IntArray(12) { it + 1 }
        val monthAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            monthArray.toTypedArray()
        )
        spMonth.adapter = monthAdapter
        spMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
                Log.d("month", parent?.getItemAtPosition(position).toString())
                selectedMonth = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val yearArray = IntArray(20) { it + 1995 }
        val yearAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            yearArray.toTypedArray()
        )
        spYear.adapter = yearAdapter
        spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
                Log.d("year", parent?.getItemAtPosition(position).toString())
                selectedYear = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

    }

    private fun setUpEditText() {
        etName.afterTextChanged { text ->
            if (text.isEmpty()) {
                etName.error = "Name cannot be empty."
            }
            checkButtonAvailable()
        }

        etPasswordFromSignUp.afterTextChanged { text ->
            if (text.isEmpty()) {
                etPasswordFromSignUp.error = "Password cannot be empty."
            }
            if (text.length < 6) {
                etPasswordFromSignUp.error = "Password need at least 6 characters."
            }
            checkButtonAvailable()
        }

    }
    private fun validate(): Boolean {
        return etName.text?.isNotEmpty() == true &&
                etPasswordFromSignUp.text?.isNotEmpty() == true &&
                etPasswordFromSignUp.text!!.length > 5 &&
                selectedGender.isNotEmpty() &&
                checkedTermAndServices
    }

    private fun checkButtonAvailable(): Boolean {
        if (validate()) {
            btnSignUpFromSignUpScreen.alpha = 1F
            return true
        } else {
            btnSignUpFromSignUpScreen.alpha = 0.5f
            return false
        }
    }
    private fun setUpTermAndCondition() {
        cbTermsAndServices.setOnClickListener {
            checkedTermAndServices = cbTermsAndServices.isChecked
            checkButtonAvailable()
            Log.d("term", checkedTermAndServices.toString())
        }
    }
}