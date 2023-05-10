package com.laundrybuoy.customer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laundrybuoy.customer.model.auth.VerifyOtpResponse.Data.LoggedInUser

@Database(
    entities = [UserStructure::class],
    version = 2
)

abstract class UserDb : RoomDatabase() {

    abstract fun getUserDAO(): UserDAO

}