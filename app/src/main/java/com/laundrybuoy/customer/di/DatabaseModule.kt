package com.laundrybuoy.customer.di

import android.content.Context
import androidx.room.Room
import com.laundrybuoy.customer.db.UserDAO
import com.laundrybuoy.customer.db.UserDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideUserDB(@ApplicationContext context: Context): UserDb {
        return Room.databaseBuilder(context, UserDb::class.java, "UserDb").build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: UserDb): UserDAO {
        return db.getUserDAO()
    }
}