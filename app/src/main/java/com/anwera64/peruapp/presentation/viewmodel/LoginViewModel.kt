package com.anwera64.peruapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anwera64.peruapp.data.Repository
import com.anwera64.peruapp.data.model.Token
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val cd: CompositeDisposable = CompositeDisposable()
    private val repository: Repository = Repository.getInstance()
    val token: MutableLiveData<Token> = MutableLiveData()

    fun performLogin(email: String, pass: String) {
        cd.add(
            repository.login(email, pass).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        token.postValue(response.body())
                    } else {
                        Log.e(this.javaClass.simpleName, response.errorBody()?.string())
                    }
                }, { error -> Log.e(this.javaClass.simpleName, error.message) })
        )
    }
}