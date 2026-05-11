package com.owifintrack.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.owifintrack.app.data.dao.OwiDao
import com.owifintrack.app.data.model.Account
import com.owifintrack.app.data.model.Category
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.Transaction

@Database(
    entities = [Account::class, Category::class, Transaction::class, Debt::class],
    version = 1,
    exportSchema = false
)
abstract class OwiDatabase : RoomDatabase() {

    // Menghubungkan database dengan Satpam (DAO)
    abstract fun owiDao(): OwiDao

    companion object {
        @Volatile
        private var INSTANCE: OwiDatabase? = null

        // Fungsi untuk memastikan hanya ada 1 instance database (Singleton)
        // Ini mencegah kebocoran memori dan tabrakan data
        fun getDatabase(context: Context): OwiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OwiDatabase::class.java,
                    "owi_fintrack_database" // Nama file brankas di HP
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}