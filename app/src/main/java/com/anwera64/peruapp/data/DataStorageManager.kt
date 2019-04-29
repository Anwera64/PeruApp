package com.anwera64.peruapp.data

import com.anwera64.peruapp.data.model.Task
import io.realm.Realm

class DataStorageManager {
    companion object {
        val instance by lazy { DataStorageManager() }
    }

    private val realm = Realm.getDefaultInstance()

    fun getTask(uid: String): Task? {
        return realm.where(Task::class.java)
            .equalTo("id", uid)
            .findFirst()
    }

    fun createTask(task: Task) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(task)
        }
    }

    fun getTasks(): ArrayList<Task> {
        val results = realm.where(Task::class.java).findAll()
        val list = ArrayList<Task>()
        list.addAll(realm.copyFromRealm(results))
        return list
    }

    fun deleteTask(uid: String) {
        realm.executeTransaction {
            it.where(Task::class.java)
                .equalTo("id", uid)
                .findFirst()
                ?.deleteFromRealm()
        }
    }
}
