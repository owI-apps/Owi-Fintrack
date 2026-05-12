package com.owifintrack.app.ui.add_transaction

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.owifintrack.app.data.database.OwiDatabase
import com.owifintrack.app.data.model.Account
import com.owifintrack.app.data.model.Category
import com.owifintrack.app.data.model.CategoryType
import com.owifintrack.app.data.model.Transaction
import com.owifintrack.app.data.model.TransactionType
import com.owifintrack.app.data.repository.OwiRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddTransactionViewModel(private val repository: OwiRepository) : ViewModel() {

    // Ambil Semua Akun (Cash, Bank, E-Wallet, dll)
    val allAccounts: StateFlow<List<Account>> = repository.getAllAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ambil Kategori Pemasukan
    val incomeCategories: StateFlow<List<Category>> = repository.getCategoriesByType(CategoryType.INCOME)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ambil Kategori Pengeluaran
    val expenseCategories: StateFlow<List<Category>> = repository.getCategoriesByType(CategoryType.EXPENSE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Fungsi Simpan Transaksi ke Database
    fun saveTransaction(
        type: TransactionType,
        amount: Double,
        note: String,
        accountId: Int, // Sekarang kita pakai accountId yang dipilih user!
        categoryId: Int
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                type = type,
                amount = amount,
                note = note,
                accountId = accountId, 
                categoryId = categoryId
            )
            repository.insertTransaction(transaction)
        }
    }
}

class AddTransactionViewModelFactory(application: Application) : ViewModelProvider.Factory {
    private val repository = OwiRepository(
        OwiDatabase.getDatabase(application).owiDao()
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}