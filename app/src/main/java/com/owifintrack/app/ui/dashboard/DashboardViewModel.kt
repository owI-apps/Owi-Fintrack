package com.owifintrack.app.ui.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.owifintrack.app.data.database.OwiDatabase
import com.owifintrack.app.data.repository.OwiRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(private val repository: OwiRepository) : ViewModel() {

    // Manajer mengambil data dari Pustakawan (Repository), lalu mengubahnya menjadi StateFlow.
    // StateFlow itu seperti tabung air yang selalu mengalirkan data terbaru ke UI secara real-time.
    // Jika transaksi baru masuk, angka-angka ini OTOMATIS berubah di layar tanpa perlu refresh!

    val netWorth: StateFlow<Double> = repository.getNetWorth()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalAssets: StateFlow<Double> = repository.getTotalAssets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalDebt: StateFlow<Double> = repository.getTotalDebt()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalReceivable: StateFlow<Double> = repository.getTotalReceivable()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}

// Ini adalah "Pabrik Pembuat Manajer" (ViewModelFactory).
// Karena ViewModel butuh Repository, dan Repository butuh Database, 
// Android butuh Factory ini untuk merangkainya saat aplikasi pertama kali menyala.

class DashboardViewModelFactory(application: Application) : ViewModelProvider.Factory {
    private val repository = OwiRepository(
        OwiDatabase.getDatabase(application).owiDao()
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}