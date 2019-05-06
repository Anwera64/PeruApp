package com.anwera64.peruapp.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.anwera64.peruapp.data.model.Task

class TaskRepository private constructor(val database: AppDatabase) {

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
        mTasks.addSource(database.taskDAO().getAll()) { taskEntities ->
            if (database.mIsDatabaseCreated.value != null) {
                mTasks.postValue(taskEntities)
            }
        }
    }

    @WorkerThread
    suspend fun insertTask(task: Task) {
        database.taskDAO().insertTask(task)
    }

    fun getTask(id: String) : Task? {
        return database.taskDAO().getTask(id)
    }

    fun filterTasks(query: String) {
        //TODO
    }

}