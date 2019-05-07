package com.anwera64.peruapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anwera64.peruapp.data.AppDatabase
import com.anwera64.peruapp.data.TaskRepository
import com.anwera64.peruapp.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val allTasks: LiveData<List<Task>>

    init {
        val db = AppDatabase.getInstance(application)
        repository = TaskRepository.getInstance(db)
        allTasks = repository.mTasks
    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteTask(task)
    }
}