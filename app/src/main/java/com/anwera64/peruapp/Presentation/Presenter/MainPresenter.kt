package com.anwera64.peruapp.presentation.presenter

import com.anwera64.peruapp.data.DataStorageManager
import com.anwera64.peruapp.data.model.Task

class MainPresenter(private val view: MainPresenterDelegate) {

    private val dsm = DataStorageManager.instance

    fun getTasks() {
         view.onTasksReady(dsm.getTasks())
    }
}

interface MainPresenterDelegate {
    fun onTasksReady(tasks: ArrayList<Task>)
}