package com.owifintrack.app.ui.debt

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.owifintrack.app.data.database.OwiDatabase
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.DebtStatus
import com.owifintrack.app.data.model.DebtType
import com.owifintrack.app.data.repository.OwiRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DebtViewModel(private val repository: OwiRepository) : ViewModel() {

    val payables: StateFlow<List<Debt>> = repository.getDebts(DebtType.PAYABLE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val receivables: StateFlow<List<Debt>> = repository.getDebts(DebtType.RECEIVABLE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveDebt(
        personName: String,
        type: DebtType,
        totalAmount: Double,
        note: String
    ) {
        viewModelScope.launch {
            val debt = Debt(
                personName = personName,
                type = type,
                totalAmount = totalAmount,
                remainingAmount = totalAmount,
                dueDate = System.currentTimeMillis(),
                status = DebtStatus.ONGOING,
                note = note
            )
            repository.insertDebt(debt)
        }
    }

    // FUNGSI BARU: Mencatat pembayaran/cicilan hutang atau piutang
    fun payDebt(debt: Debt, paymentAmount: Double) {
        viewModelScope.launch {
            val newRemaining = debt.remainingAmount - paymentAmount
            
            // Jika sisa menjadi 0 atau minus, status berubah jadi LUNAS (PAID)
            val newStatus = if (newRemaining <= 0.0) DebtStatus.PAID else DebtStatus.ONGOING
            
            // Pastikan sisa tidak minus (jika bayar lebih, dianggap lunas sempurna)
            val finalRemaining = if (newRemaining < 0.0) 0.0 else newRemaining

            val updatedDebt = debt.copy(
                remainingAmount = finalRemaining,
                status = newStatus
            )
            repository.updateDebt(updatedDebt)
        }
    }
}

class DebtViewModelFactory(application: Application) : ViewModelProvider.Factory {
    private val repository = OwiRepository(
        OwiDatabase.getDatabase(application).owiDao()
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DebtViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DebtViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}