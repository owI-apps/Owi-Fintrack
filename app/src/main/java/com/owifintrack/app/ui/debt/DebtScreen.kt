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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.owifintrack.app.data.model.Debt
import com.owifintrack.app.data.model.DebtStatus
import com.owifintrack.app.data.model.DebtType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtScreen(
    onBackClick: () -> Unit,
    viewModel: DebtViewModel = viewModel(factory = DebtViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val payables by viewModel.payables.collectAsState()
    val receivables by viewModel.receivables.collectAsState()

    // State Form Utama
    var personName by remember { mutableStateOf("") }
    var totalAmount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedDebtType by remember { mutableStateOf(DebtType.PAYABLE) }

    // State Dialog Pembayaran
    var showPayDialog by remember { mutableStateOf(false) }
    var selectedDebtForPayment by remember { mutableStateOf<Debt?>(null) }
    var paymentAmount by remember { mutableStateOf("") }

    // Dialog Konfirmasi Pembayaran
    if (showPayDialog && selectedDebtForPayment != null) {
        AlertDialog(
            onDismissRequest = { showPayDialog = false },
            title = { Text("Bayar / Cicil") },
            text = {
                Column {
                    Text("Sisa yang harus dibayar: Rp %,.0f".format(selectedDebtForPayment!!.remainingAmount).replace(',', '.'))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = paymentAmount,
                        onValueChange = { paymentAmount = it },
                        label = { Text("Nominal Bayar (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val payAmount = paymentAmount.toDoubleOrNull() ?: 0.0
                        if (payAmount > 0) {
                            viewModel.payDebt(selectedDebtForPayment!!, payAmount)
                            showPayDialog = false
                            paymentAmount = ""
                            selectedDebtForPayment = null
                        }
                    },
                    enabled = paymentAmount.isNotBlank()
                ) {
                    Text("Simpan Pembayaran")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showPayDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

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

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { selectedDebtType = DebtType.PAYABLE },
                                modifier = Modifier.weight(1f),
                                colors = if (selectedDebtType == DebtType.PAYABLE) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.outlinedButtonColors()
                            ) { Text("Hutang", color = if (selectedDebtType == DebtType.PAYABLE) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.error) }
                            OutlinedButton(
                                onClick = { selectedDebtType = DebtType.RECEIVABLE },
                                modifier = Modifier.weight(1f),
                                colors = if (selectedDebtType == DebtType.RECEIVABLE) ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)) else ButtonDefaults.outlinedButtonColors()
                            ) { Text("Piutang", color = if (selectedDebtType == DebtType.RECEIVABLE) Color.White else Color(0xFF1565C0)) }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(value = personName, onValueChange = { personName = it }, label = { Text("Nama Pihak Terkait") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        OutlinedTextField(value = totalAmount, onValueChange = { totalAmount = it }, label = { Text("Nominal (Rp)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Catatan (Opsional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val amount = totalAmount.toDoubleOrNull() ?: 0.0
                                if (personName.isNotBlank() && amount > 0) {
                                    viewModel.saveDebt(personName, selectedDebtType, amount, note)
                                    personName = ""; totalAmount = ""; note = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = personName.isNotBlank() && totalAmount.isNotBlank()
                        ) { Text("Simpan") }
                    }
                }
            }

            // DAFTAR HUTANG
            item { Text("Hutang Saya (Yang harus saya bayar)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error) }
            if (payables.isEmpty()) {
                item { Text("Tidak ada hutang aktif.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                items(payables) { debt ->
                    DebtItem(
                        debt = debt,
                        isPayable = true,
                        onPayClick = {
                            selectedDebtForPayment = debt
                            showPayDialog = true
                        }
                    )
                }
            }

            // DAFTAR PIUTANG
            item { Spacer(modifier = Modifier.height(8.dp)); Text("Piutang Saya (Yang harus dibayar ke saya)", style = MaterialTheme.typography.titleMedium, color = Color(0xFF1565C0)) }
            if (receivables.isEmpty()) {
                item { Text("Tidak ada piutang aktif.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                items(receivables) { debt ->
                    DebtItem(
                        debt = debt,
                        isPayable = false,
                        onPayClick = {
                            selectedDebtForPayment = debt
                            showPayDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DebtItem(debt: Debt, isPayable: Boolean, onPayClick: () -> Unit) {
    val cardColor = if (isPayable) Color(0xFFFFEBEE) else Color(0xFFE3F2FD)
    val textColor = if (isPayable) Color(0xFFC62828) else Color(0xFF1565C0)
    val isLunas = debt.status == DebtStatus.PAID

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(debt.personName, style = MaterialTheme.typography.titleMedium, color = textColor)
                if (debt.note.isNotBlank()) Text(debt.note, style = MaterialTheme.typography.bodySmall, color = textColor.copy(alpha = 0.7f))
                
                if (isLunas) {
                    Text("LUNAS ✓", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                } else {
                    Text("Sisa: Rp %,.0f".format(debt.remainingAmount).replace(',', '.'), style = MaterialTheme.typography.bodyMedium, color = textColor)
                }
            }
            
            // Tombol Bayar hanya muncul jika belum lunas
            if (!isLunas) {
                OutlinedButton(
                    onClick = onPayClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Bayar/Cicil", fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp))
                }
            }
        }
    }
}