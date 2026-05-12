package com.owifintrack.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase // PERBAIKAN: Import yang benar untuk Room Callback
import com.owifintrack.app.data.dao.OwiDao
import com.owifintrack.app.data.model.Account
import com.owifintrack.app.data.model.AccountType
import com.owifintrack.app.data.model.Category
import com.owifintrack.app.data.model.CategoryType
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Account::class, Category::class, Transaction::class, Debt::class],
    version = 1,
    exportSchema = false
)
abstract class OwiDatabase : RoomDatabase() {

    abstract fun owiDao(): OwiDao

    companion object {
        @Volatile
        private var INSTANCE: OwiDatabase? = null

        fun getDatabase(context: Context): OwiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OwiDatabase::class.java,
                    "owi_fintrack_database"
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Callback untuk mengisi data awal agar pengguna bisa langsung mencatat
    private class DatabaseCallback(
        private val context: Context
    ) : Callback() {
        // PERBAIKAN: Menggunakan SupportSQLiteDatabase
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                populateDatabase(getDatabase(context).owiDao())
            }
        }

        suspend fun populateDatabase(dao: OwiDao) {
            // 1. Buat Akun Default
            dao.insertAccount(Account(id = 1, name = "Dompet Cash", type = AccountType.CASH, initialBalance = 0.0))
            dao.insertAccount(Account(id = 2, name = "Bank BCA", type = AccountType.BANK, initialBalance = 0.0))

            // 2. Buat Kategori Pemasukan Default
            dao.insertCategory(Category(id = 1, name = "Gaji", type = CategoryType.INCOME))
            dao.insertCategory(Category(id = 2, name = "Freelance", type = CategoryType.INCOME))
            dao.insertCategory(Category(id = 3, name = "Hadiah", type = CategoryType.INCOME))

            // 3. Buat Kategori Pengeluaran Default
            dao.insertCategory(Category(id = 4, name = "Makanan & Minuman", type = CategoryType.EXPENSE))
            dao.insertCategory(Category(id = 5, name = "Transportasi", type = CategoryType.EXPENSE))
            dao.insertCategory(Category(id = 6, name = "Belanja", type = CategoryType.EXPENSE))
            dao.insertCategory(Category(id = 7, name = "Tagihan", type = CategoryType.EXPENSE))
        }
    }
}