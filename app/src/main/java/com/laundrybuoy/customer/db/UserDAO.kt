package com.laundrybuoy.customer.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun signIn(user: UserStructure)

    @Query("DELETE FROM UserStructure")
    fun signOut()

    @Query("SELECT * FROM UserStructure LIMIT 1")
    fun getLoggedInUserLive(): LiveData<UserStructure>
}