package com.anwera64.peruapp.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.checkEditText(message: String): Boolean {
    if (editText?.text.isNullOrEmpty()) {
        error = message
        isErrorEnabled = true
        return true
    }
    return false
}

