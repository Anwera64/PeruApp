package com.anwera64.peruapp.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.anwera64.peruapp.data.local.AppDatabase
import com.anwera64.peruapp.data.model.Task

class TaskRepository private constructor(private val database: AppDatabase) {

    var mTasks: MediatorLiveData<List<Task>> = MediatorLiveData()

    companion object {
        @Volatile
        private var INSTANCE: TaskRepository? = null

        fun getInstance(database: AppDatabase): TaskRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = TaskRepository(database)
                INSTANCE = instance
                return instance
            }
        }
    }

    init {
        mTasks.addSource(database.taskDAO().getAll()) { taskEntities -> postTasks(taskEntities) }
    }

    private fun postTasks(taskEntities: List<Task>) {
        if (database.mIsDatabaseCreated.value != null) {
            mTasks.postValue(taskEntities)
        }
    }

    @WorkerThread
    suspend fun insertTask(task: Task) {
        database.taskDAO().insertTask(task)
    }

    @WorkerThread
    suspend fun deleteTask(task: Task) {
        database.taskDAO().deleteTask(task)
    }

    fun getTask(id: String): Task? {
        return database.taskDAO().getTask(id)
    }
}