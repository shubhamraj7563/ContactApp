package com.example.myapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "contacts_table")
data class Contact (
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    var name: String,
    var phone: String,
    var email: String,
    var isActive: Boolean,
    var dateofCreation : Long,
    var image : ByteArray? = null

)