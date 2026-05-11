package com.owifintrack.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.owifintrack.app.data.model.Account
import com.owifintrack.app.data.model.Category
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.DebtStatus
import com.owifintrack.app.data.model.DebtType
import com.owifintrack.app.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface OwiDao {

    // ================= ACCOUNT (AKUN) =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>

    // ================= CATEGORY (KATEGORI) =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE type = :type")
    fun getCategoriesByType(type: String): Flow<List<Category>>

    // ================= TRANSACTION (TRANSAKSI) =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    // ================= DEBT (HUTANG & PIUTANG) =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: Debt)

    @Query("SELECT * FROM debts WHERE type = :type AND status != :excludeStatus")
    fun getDebtsByType(type: DebtType, excludeStatus: DebtStatus = DebtStatus.PAID): Flow<List<Debt>>
}