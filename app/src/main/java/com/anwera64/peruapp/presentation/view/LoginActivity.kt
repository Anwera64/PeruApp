package com.anwera64.peruapp.presentation.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anwera64.peruapp.R
import com.anwera64.peruapp.data.local.Preferences
import com.anwera64.peruapp.data.model.Token
import com.anwera64.peruapp.extensions.checkEditText
import com.anwera64.peruapp.extensions.checkEmail
import com.anwera64.peruapp.presentation.viewmodel.LoginViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var prefs: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = Preferences.getInstance(this, Gson())

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.token.observe(this, Observer { token -> onToken(token) })

        btnLogin.setOnClickListener { checkForm() }
    }

    private fun onToken(token: Token?) {
        prefs.setToken(token)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun checkForm() {
        if (!tilEmail.checkEmail(getString(R.string.error_empty), getString(R.string.error_email))) return
        if (!tilPass.checkEditText(getString(R.string.error_empty))) return

        viewModel.performLogin(tilEmail.editText?.text.toString(), tilPass.editText?.text.toString())
    }
}