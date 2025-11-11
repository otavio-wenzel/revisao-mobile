package com.example.app_ajudai.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_ajudai.feature.favor.data.Favor
import com.example.app_ajudai.feature.favor.data.FavorDao
import com.example.app_ajudai.feature.inbox.data.HelpRequest
import com.example.app_ajudai.feature.inbox.data.HelpRequestDao
import com.example.app_ajudai.feature.auth.data.User
import com.example.app_ajudai.feature.auth.data.UserDao

/**
 * Banco Room central do app. Version = 5 (migrations destrutivas habilitadas).
 * Entidades: Favor, User, HelpRequest.
 */
@Database(entities = [Favor::class, User::class, HelpRequest::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favorDao(): FavorDao
    abstract fun userDao(): UserDao
    abstract fun helpRequestDao(): HelpRequestDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        /**
         * Singleton thread-safe. Usa fallbackToDestructiveMigration() para simplificar dev.
         * (Em produção, preferir migrations explícitas.)
         */
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ajudai.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
