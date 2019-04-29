package com.anwera64.peruapp.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

class Task (
    @PrimaryKey
    val id: String,
    var title: String,
    var detail: String,
    val creationDate: Date,
    val notificationDate: Date
) : RealmObject()