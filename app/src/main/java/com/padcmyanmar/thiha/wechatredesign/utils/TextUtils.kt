package com.padcmyanmar.thiha.wechatredesign.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.validateEmail() : Boolean{
    if (this.text.isEmpty()) {
        this.error = "Email cannot be empty."
    } else {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()){
            this.error = "invalid email"
            return false
        }
        return true
    }
    return false
}