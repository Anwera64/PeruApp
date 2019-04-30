package com.anwera64.peruapp.presentation.presenter

import com.anwera64.peruapp.data.DataStorageManager
import com.anwera64.peruapp.data.model.Task

class NewTaskPresenter(private val view: NewTaskDelegate) {

    private val dsm = DataStorageManager.instance

    fun saveTask(task: Task) {
        dsm.createTask(task)
        view.onTaskCreated(task.id)
    }
}

interface NewTaskDelegate {
    fun onTaskCreated(id: String)
}