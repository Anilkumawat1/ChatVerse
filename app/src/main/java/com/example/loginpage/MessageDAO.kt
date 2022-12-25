package com.example.loginpage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDAO {
    @Query("SELECT * FROM messages_local")
    fun getAll() : LiveData<List<localMessages>>

    @Query("SELECT * FROM messages_local where ((sender =:loginuser AND receiver =:friend)OR(sender =:friend AND receiver =:loginuser)) ORDER BY timestamp ASC")
     fun getAllMess(loginuser:String,friend : String) : LiveData<List<localMessages>>

    @Query("select * from messages_local where id in (select max(id) from messages_local group by user) ORDER BY id DESC")
    fun getLast1Mess() : LiveData<List<localMessages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localmess: localMessages)


}