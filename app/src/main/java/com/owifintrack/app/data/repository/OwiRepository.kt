package com.owifintrack.app.data.repository

import com.owifintrack.app.core.FinancialCalculator // INI YANG TADI TERTINGGAL
import com.owifintrack.app.data.dao.OwiDao
import com.owifintrack.app.data.model.Account
import com.owifintrack.app.data.model.Category
import com.owifintrack.app.data.model.CategoryType
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.DebtType
import com.owifintrack.app.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class OwiRepository(private val owiDao: OwiDao) {

    // ================= AMBIL DATA DARI BRANKAS =================
    
    fun getAllAccounts(): Flow<List<Account>> = owiDao.getAllAccounts()
    fun getAllTransactions(): Flow<List<Transaction>> = owiDao.getAllTransactions()
    fun getDebts(type: DebtType): Flow<List<Debt>> = owiDao.getDebtsByType(type)
    fun getCategoriesByType(type: CategoryType): Flow<List<Category>> = owiDao.getCategoriesByType(type.name)

    // ================= SIMPAN DATA KE BRANKAS =================

    suspend fun insertAccount(account: Account) = owiDao.insertAccount(account)
    suspend fun insertCategory(category: Category) = owiDao.insertCategory(category)
    suspend fun insertTransaction(transaction: Transaction) = owiDao.insertTransaction(transaction)
    suspend fun insertDebt(debt: Debt) = owiDao.insertDebt(debt)

    // ================= KALKULASI EXPERT (MENGHIDUPKAN OTAK) =================
    // Ini adalah fungsi kunci yang memastikan bayangan keuangan pengguna akurat.

    fun getNetWorth(): Flow<Double> {
        // Kita gabungkan 3 sumber data secara real-time menggunakan Flow.
        // Jika salah satu data berubah, Net Worth akan otomatis terhitung ulang!
        return combine(
            getAllAccounts(),
            getAllTransactions(),
            getDebts(DebtType.PAYABLE)
        ) { accounts, transactions, debts ->
            FinancialCalculator.calculateNetWorth(accounts, transactions, debts)
        }
    }

    fun getTotalAssets(): Flow<Double> {
        return combine(
            getAllAccounts(),
            getAllTransactions()
        ) { accounts, transactions ->
            FinancialCalculator.calculateTotalAssets(accounts, transactions)
        }
    }

    fun getTotalDebt(): Flow<Double> {
        return getDebts(DebtType.PAYABLE) // Flow otomatis menjumlahkan yang berstatus ONGOING di calculator nanti
    }

    fun getTotalReceivable(): Flow<Double> {
        return getDebts(DebtType.RECEIVABLE)
    }
}