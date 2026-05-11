package com.owifintrack.app.core

import com.owifintrack.app.data.model.Account
import com.owifintrack.app.data.model.AccountType
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.DebtType
import com.owifintrack.app.data.model.DebtStatus
import com.owifintrack.app.data.model.Transaction
import com.owifintrack.app.data.model.TransactionType

/**
 * Otak Kalkulator Murni Owi Fintrack.
 * Tidak ada taksiran. Tidak ada penyusutan. Hanya angka nyata dari input user.
 * Kelas ini menjamin perhitungan saldo dan kekayaan bersih 100% akurat.
 */
object FinancialCalculator {

    /**
     * Menghitung Total Aset Nyata (Uang yang benar-benar ada dan bisa diakses)
     * Rumus: Penjumlahan seluruh saldo akun (Cash, Bank, E-Wallet, dll)
     */
    fun calculateTotalAssets(accounts: List<Account>, transactions: List<Transaction>): Double {
        val accountBalances = mutableMapOf<Int, Double>()

        // Set saldo awal dari setiap akun
        for (account in accounts) {
            accountBalances[account.id] = account.initialBalance
        }

        // Hitung dampak transaksi terhadap saldo akun
        for (transaction in transactions) {
            when (transaction.type) {
                TransactionType.INCOME -> {
                    // Uang masuk ke akun tertentu
                    val currentBalance = accountBalances[transaction.accountId] ?: 0.0
                    accountBalances[transaction.accountId] = currentBalance + transaction.amount
                }
                TransactionType.EXPENSE -> {
                    // Uang keluar dari akun tertentu
                    val currentBalance = accountBalances[transaction.accountId] ?: 0.0
                    accountBalances[transaction.accountId] = currentBalance - transaction.amount
                }
                TransactionType.TRANSFER -> {
                    // Uang pindah antar akun (Total Aset TIDAK berubah, hanya lokasi yang berpindah)
                    val fromBalance = accountBalances[transaction.fromAccountId] ?: 0.0
                    val toBalance = accountBalances[transaction.toAccountId] ?: 0.0
                    
                    accountBalances[transaction.fromAccountId] = fromBalance - transaction.amount
                    accountBalances[transaction.toAccountId] = toBalance + transaction.amount
                }
            }
        }

        // Jumlahkan semua saldo akhir akun
        return accountBalances.values.sum()
    }

    /**
     * Menghitung Total Hutang (Uang yang harus keluar)
     * Hanya menghitung hutang yang statusnya ONGOING (belum lunas)
     */
    fun calculateTotalDebt(debts: List<Debt>): Double {
        return debts
            .filter { it.type == DebtType.PAYABLE && it.status != DebtStatus.PAID }
            .sumOf { it.remainingAmount }
    }

    /**
     * Menghitung Total Piutang (Uang yang harus masuk)
     * Hanya menghitung piutang yang statusnya ONGOING (belum dilunasi orang lain)
     */
    fun calculateTotalReceivable(debts: List<Debt>): Double {
        return debts
            .filter { it.type == DebtType.RECEIVABLE && it.status != DebtStatus.PAID }
            .sumOf { it.remainingAmount }
    }

    /**
     * Menghitung Kekayaan Bersih (Net Worth) - Puncak dari Bayangan Keuangan
     * Rumus: (Aset Nyata + Piutang) - Hutang
     */
    fun calculateNetWorth(
        accounts: List<Account>,
        transactions: List<Transaction>,
        debts: List<Debt>
    ): Double {
        val totalAssets = calculateTotalAssets(accounts, transactions)
        val totalDebt = calculateTotalDebt(debts)
        val totalReceivable = calculateTotalReceivable(debts)

        return (totalAssets + totalReceivable) - totalDebt
    }
}