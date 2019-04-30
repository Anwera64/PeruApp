package com.anwera64.peruapp.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*
import kotlin.math.exp

class Task (
    @PrimaryKey
    val id: String,
    var title: String,
    var detail: String,
    val creationDate: Date,
    val expirationDate: Date,
    val notificationDate: Date?
) : RealmObject() {
    constructor(id: String, title: String, detail: String, creationDate: Date, expirationDate: Date)
            : this(id, title, detail, creationDate, expirationDate, null)
}