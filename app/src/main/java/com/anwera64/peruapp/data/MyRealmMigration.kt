package com.anwera64.peruapp.data

import io.realm.DynamicRealm
import io.realm.RealmMigration
import java.util.*

class MyRealmMigration: RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        if (oldVersion < 1) {
            schema.create("Task")
                .addField("title", String::class.java)
                .addField("detail", String::class.java)
                .addField("creationDate", Date::class.java)
                .addField("notificationDate", Date::class.java)
                .addPrimaryKey("id")
        }
    }
}