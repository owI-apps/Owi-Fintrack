package com.owifintrack.app.ui.debt

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.DebtType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtScreen(
    onBackClick: () -> Unit,
    viewModel: DebtViewModel = viewModel(factory = DebtViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val payables by viewModel.payables.collectAsState()
    val receivables by viewModel.receivables.collectAsState()

    // State Form
    var personName by remember { mutableStateOf("") }
    var totalAmount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedDebtType by remember { mutableStateOf(DebtType.PAYABLE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hutang & Piutang") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // FORM TAMBAH HUTANG/PIUTANG
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Catat Hutang/Piutang Baru", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Pilihan Tipe
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { selectedDebtType = DebtType.PAYABLE },
                                modifier = Modifier.weight(1f),
                                colors = if (selectedDebtType == DebtType.PAYABLE) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.outlinedButtonColors()
                            ) {
                                Text("Hutang", color = if (selectedDebtType == DebtType.PAYABLE) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.error)
                            }
                            OutlinedButton(
                                onClick = { selectedDebtType = DebtType.RECEIVABLE },
                                modifier = Modifier.weight(1f),
                                colors = if (selectedDebtType == DebtType.RECEIVABLE) ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)) else ButtonDefaults.outlinedButtonColors()
                            ) {
                                Text("Piutang", color = if (selectedDebtType == DebtType.RECEIVABLE) Color.White else Color(0xFF1565C0))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = personName,
                            onValueChange = { personName = it },
                            label = { Text("Nama Pihak Terkait") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = totalAmount,
                            onValueChange = { totalAmount = it },
                            label = { Text("Nominal (Rp)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Catatan (Opsional)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val amount = totalAmount.toDoubleOrNull() ?: 0.0
                                if (personName.isNotBlank() && amount > 0) {
                                    viewModel.saveDebt(personName, selectedDebtType, amount, note)
                                    personName = ""
                                    totalAmount = ""
                                    note = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = personName.isNotBlank() && totalAmount.isNotBlank()
                        ) {
                            Text("Simpan")
                        }
                    }
                }
            }

            // DAFTAR HUTANG (PAYABLES)
            item {
                Text("Hutang Saya (Yang harus saya bayar)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (payables.isEmpty()) {
                item { Text("Tidak ada hutang aktif.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                items(payables) { debt ->
                    DebtItem(debt, isPayable = true)
                }
            }

            // DAFTAR PIUTANG (RECEIVABLES)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Piutang Saya (Yang harus dibayar ke saya)", style = MaterialTheme.typography.titleMedium, color = Color(0xFF1565C0))
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (receivables.isEmpty()) {
                item { Text("Tidak ada piutang aktif.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                items(receivables) { debt ->
                    DebtItem(debt, isPayable = false)
                }
            }
        }
    }
}

@Composable
fun DebtItem(debt: Debt, isPayable: Boolean) {
    val cardColor = if (isPayable) Color(0xFFFFEBEE) else Color(0xFFE3F2FD)
    val textColor = if (isPayable) Color(0xFFC62828) else Color(0xFF1565C0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(debt.personName, style = MaterialTheme.typography.titleMedium, color = textColor)
                if (debt.note.isNotBlank()) Text(debt.note, style = MaterialTheme.typography.bodySmall, color = textColor.copy(alpha = 0.7f))
            }
            Text(
                text = "Rp %,.0f".format(debt.remainingAmount).replace(',', '.'),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
        }
    }
}