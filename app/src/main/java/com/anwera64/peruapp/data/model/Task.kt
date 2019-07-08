package com.anwera64.peruapp.data.model

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Task(
    val id: String,
    val title: String,
    @SerializedName("description") val detail: String,
    @SerializedName("date") val creationDate: Date,
    val expirationDate: Date,
    val notificationDate: Date?
) : Serializable {
    @Ignore
    var isSelected: Boolean = false
}
