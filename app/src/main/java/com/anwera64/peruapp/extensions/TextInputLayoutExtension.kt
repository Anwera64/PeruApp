package com.anwera64.peruapp.extensions

import android.support.design.widget.TextInputLayout

fun TextInputLayout.checkEditText(message: String): Boolean {
    if (editText?.text.isNullOrEmpty()) {
        error = message
        isErrorEnabled = true
        return true
    }
    return false
}