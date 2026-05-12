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

    // Ambil daftar Hutang (Uang kita yang keluar/masih harus dibayar)
    val payables: StateFlow<List<Debt>> = repository.getDebts(DebtType.PAYABLE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ambil daftar Piutang (Uang orang lain yang harus masuk ke kita)
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
                remainingAmount = totalAmount, // Karena baru dibuat, sisa = total
                dueDate = System.currentTimeMillis(), // Sementara pakai tanggal hari ini
                status = DebtStatus.ONGOING,
                note = note
            )
            repository.insertDebt(debt)
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