package com.anwera64.peruapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.anwera64.peruapp.data.local.AppDatabase
import com.anwera64.peruapp.data.Repository
import com.anwera64.peruapp.data.model.Task
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    val allTasks: LiveData<List<Task>>
    private val cd: CompositeDisposable

    init {
        val db = AppDatabase.getInstance(application)
        repository = Repository.getInstance(db)
        allTasks = repository.mTasks
        cd = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteTask(task)
    }
}