package com.anwera64.peruapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.anwera64.peruapp.data.model.Token
import com.google.gson.Gson

class Preferences private constructor(context: Context, private val gson: Gson) {

    companion object {
        private const val NAME = "appPreference"

        private const val KEY_TOKEN = "token"

        private var instance: Preferences? = null

        fun getInstance(context: Context, gson: Gson) : Preferences {
            if (instance == null) {
                instance = Preferences(context, gson)
            }
            return instance!!
        }
    }

    private val prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun setToken(token: Token?) {
        val json = gson.toJson(token)
        prefs.edit().putString(KEY_TOKEN, json).apply()
    }

    fun getToken(): Token {
        val json = prefs.getString(KEY_TOKEN, null)
        return gson.fromJson<Token>(json, Token::class.java)
    }

}