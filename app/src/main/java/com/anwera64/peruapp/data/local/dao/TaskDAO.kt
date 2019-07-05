package com.anwera64.peruapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.anwera64.peruapp.data.model.Task

@Dao
interface TaskDAO {
    @Query("SELECT * FROM task WHERE id LIKE :uid LIMIT 1 ")
    fun getTask(uid: String): Task?

    @Query("SELECT * FROM task ORDER BY expiration_date")
    fun getAll(): LiveData<List<Task>>

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}