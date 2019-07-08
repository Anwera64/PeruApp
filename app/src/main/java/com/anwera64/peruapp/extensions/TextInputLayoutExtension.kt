package com.anwera64.peruapp.extensions

import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.checkEditText(message: String): Boolean {
    if (editText?.text.isNullOrEmpty()) {
        error = message
        isErrorEnabled = true
        return false
    }
    return true
}

fun TextInputLayout.checkEmail(emptyMessage: String, emailMessage: String): Boolean {
    if (!checkEditText(emptyMessage)) return false

    if (!editText?.text.toString().isValidEmail()) {
        error = emailMessage
        enableErrorAndRequestFocus()
        return false
    }

    isErrorEnabled = false
    return true
}

private fun TextInputLayout.enableErrorAndRequestFocus() {
    isErrorEnabled = true
    requestFocus()
}


fun String.isValidEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()