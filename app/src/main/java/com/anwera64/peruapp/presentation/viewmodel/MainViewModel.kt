package com.anwera64.peruapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anwera64.peruapp.data.Repository
import com.anwera64.peruapp.data.local.AppDatabase
import com.anwera64.peruapp.data.model.Task
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    val allTasks: MutableLiveData<ArrayList<Task>>
    private val cd: CompositeDisposable

    init {
        val db = AppDatabase.getInstance(application)
        repository = Repository.getInstance(db)
        allTasks = MutableLiveData()
        cd = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    fun getTasks(token: String) {
        cd.add(repository.tasks(token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    val list = ArrayList<Task>()
                    list.addAll(response.body()!!)
                    allTasks.postValue(list)
                } else {
                    Log.e(this.javaClass.simpleName, response.errorBody()?.string())
                }
            }, { error -> Log.e(this.javaClass.simpleName, error.message, error) })
        )
    }

    fun insertTask(task: Task, token: String) {
        cd.add(repository.createTask(task.title, task.detail, token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    allTasks.value?.add(response.body()!!)
                } else {
                    Log.e(this.javaClass.simpleName, response.errorBody()?.string())
                }
            }, { error -> Log.e(this.javaClass.simpleName, error.message, error) })
        )
    }

    fun deleteTask(task: Task, token: String) {
        cd.add(repository.deleteTask(task.id, token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    allTasks.value?.remove(task)
                } else {
                    Log.e(this.javaClass.simpleName, response.errorBody()?.string())
                }
            }, { error -> Log.e(this.javaClass.simpleName, error.message, error) })
        )
    }
}