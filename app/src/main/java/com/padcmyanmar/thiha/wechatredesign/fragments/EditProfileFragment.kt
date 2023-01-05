package com.padcmyanmar.thiha.wechatredesign.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.delegates.EditProfileDelegate
import com.padcmyanmar.thiha.wechatredesign.utils.afterTextChanged
import kotlinx.android.synthetic.main.fragment_edit_profile.*


class EditProfileFragment : DialogFragment() {


    private lateinit var editDialog: Dialog
    private var mEditProfileDelegate: EditProfileDelegate? = null


    companion object{
        const val TAG = "PurchaseConfirmationDialog"

        var selectedName: String = ""
        var selectedPhone: String = ""
        var selectedDay: String = "1"
        var selectedMonth: String = "1"
        var selectedYear: String = "1995"
        var selectedGender: String = ""

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setDelegate(delegate: EditProfileDelegate){
        mEditProfileDelegate = delegate
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        editDialog = Dialog(requireContext())
        editDialog.setCancelable(true)
        editDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        editDialog.setContentView(R.layout.fragment_edit_profile)
        setUpDateOfBirth(editDialog)
        setUpGender(editDialog)
        setUpEditText(editDialog)
        setUpListeners(editDialog)



        return editDialog
    }

    private fun setUpEditText(dialog: Dialog) {
        dialog.etNameFromEditProfile.setText(selectedName)
        dialog.etPhoneFromEditProfile.setText(selectedPhone)
    }

    private fun setUpListeners(dialog: Dialog) {

        dialog.etNameFromEditProfile.afterTextChanged{
            selectedName = it
        }

        dialog.etPhoneFromEditProfile.afterTextChanged {
            selectedPhone = it
        }
        dialog.btnCancelFromEditProfile.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnSave.setOnClickListener {
            mEditProfileDelegate?.onSaveProfile(
                name = selectedName,
                phone = selectedPhone,
                dob = selectedDay.plus("/").plus(selectedMonth).plus("/").plus(selectedYear),
                gender = selectedGender
            )
            dialog.dismiss()
        }
    }

    private fun setUpDateOfBirth(dialog: Dialog) {
        val dayArray = IntArray(30) { it + 1 }
        val dayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            dayArray.toTypedArray()
        )

        dialog.spDayFromEditProfile.adapter = dayAdapter
        dialog.spDayFromEditProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        dialog.spDayFromEditProfile.setSelection(dayArray.indexOf(selectedDay.toIntOrNull() ?: 1))



        val monthArray = IntArray(12) { it + 1 }
        val monthAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            monthArray.toTypedArray()
        )
        dialog.spMonthFromEditProfile.adapter = monthAdapter
        dialog.spMonthFromEditProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        dialog.spMonthFromEditProfile.setSelection(monthArray.indexOf(selectedMonth.toIntOrNull() ?: 1))


        val yearArray = IntArray(20) { it + 1995 }
        val yearAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            yearArray.toTypedArray()
        )
        dialog.spYearFromEditProfile.adapter = yearAdapter
        dialog.spYearFromEditProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        dialog.spYearFromEditProfile.setSelection(yearArray.indexOf(selectedYear.toIntOrNull() ?: 1996))


    }

    private fun setUpGender(dialog: Dialog) {
        Log.d("selectedGender", selectedGender)
        when (selectedGender) {
            "Male" -> {
                dialog.radioButtonGroupForGenderFromEditProfile.check(R.id.rBtnMaleFromEditProfile)
            }
            "Female" -> {
                dialog.radioButtonGroupForGenderFromEditProfile.check(R.id.rBtnFemaleFromEditProfile)
            }
            "Other" -> {
                dialog.radioButtonGroupForGenderFromEditProfile.check(R.id.rBtnOtherFromEditProfile)
            }
        }
        dialog.radioButtonGroupForGenderFromEditProfile.setOnCheckedChangeListener { radioGroup, id ->

            when (id) {
                R.id.rBtnMaleFromEditProfile -> {
                    selectedGender = dialog.rBtnMaleFromEditProfile.text.toString()
                    Log.d("gender", selectedGender)

                }
                R.id.rBtnFemaleFromEditProfile -> {
                    selectedGender = dialog.rBtnFemaleFromEditProfile.text.toString()
                    Log.d("gender", selectedGender)

                }
                R.id.rBtnOtherFromEditProfile -> {
                    selectedGender = dialog.rBtnOtherFromEditProfile.text.toString()
                    Log.d("gender", selectedGender)

                }
                else -> {
                    Log.d("gender", "cannot get gender")

                }

            }

        }
    }


}