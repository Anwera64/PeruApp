package com.anwera64.peruapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task")
data class Task(
    @PrimaryKey val id: String,
    val title: String,
    val detail: String,
    @ColumnInfo(name = "creation_date") val creationDate: Long,
    @ColumnInfo(name = "expiration_date") val expirationDate: Long,
    @ColumnInfo(name = "notification_date") val notificationDate: Long?
) : Serializable {
    @Ignore
    var isSelected: Boolean = false
}
