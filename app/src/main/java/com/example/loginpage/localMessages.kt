package com.example.loginpage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages_local")
data class localMessages(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    var content : String?,
    val sender: String?,
    val receiver : String?,
    val timestamp : String?,
    val user : String
)