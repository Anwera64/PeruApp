package com.anwera64.peruapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.anwera64.peruapp.data.Repository
import com.anwera64.peruapp.data.model.Pageable
import com.anwera64.peruapp.data.model.Task
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository.getInstance()
    val taskPageable: MutableLiveData<Pageable<Task>> = MutableLiveData()
    private val cd: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    fun getTasks(token: String, page: Int = 0) {
        cd.add(repository.tasks(token, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    if (taskPageable.value == null)
                        taskPageable.postValue(response.body())
                    else {
                        response.body()?.run {
                            taskPageable.value!!.content.addAll(content)
                            taskPageable.value!!.isLast = isLast
                            taskPageable.value!!.page = page
                        }
                    }
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
                    taskPageable.value?.content?.add(0, response.body()!!)
                    taskPageable.postValue(taskPageable.value)
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
                    taskPageable.value?.content?.remove(task)
                    taskPageable.postValue(taskPageable.value)
                } else {
                    Log.e(this.javaClass.simpleName, response.errorBody()?.string())
                }
            }, { error -> Log.e(this.javaClass.simpleName, error.message, error) })
        )
    }
}