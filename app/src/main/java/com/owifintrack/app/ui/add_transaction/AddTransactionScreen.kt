package com.owifintrack.app.ui.add_transaction

import android.app.Application
import androidx.compose.foundation.layout.*
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
import com.owifintrack.app.data.model.CategoryType
import com.owifintrack.app.data.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val viewModel: AddTransactionViewModel = viewModel(
        factory = AddTransactionViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    
    // State Akun
    var selectedAccountId by remember { mutableIntStateOf(1) }
    var fromAccountId by remember { mutableIntStateOf(1) } // Khusus Transfer
    var toAccountId by remember { mutableIntStateOf(2) }   // Khusus Transfer
    var expandedAccountMenu by remember { mutableStateOf(false) }
    var expandedFromAccountMenu by remember { mutableStateOf(false) }
    var expandedToAccountMenu by remember { mutableStateOf(false) }
    val accounts = viewModel.allAccounts.collectAsState().value

    // State Kategori
    var selectedCategoryId by remember { mutableIntStateOf(0) }
    var expandedCategoryMenu by remember { mutableStateOf(false) }
    val categories = if (transactionType == TransactionType.INCOME) {
        viewModel.incomeCategories.collectAsState().value
    } else {
        viewModel.expenseCategories.collectAsState().value
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Transaksi") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Pilihan Tipe: Pengeluaran, Pemasukan, Transfer
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedButton(
                    onClick = { transactionType = TransactionType.EXPENSE },
                    modifier = Modifier.weight(1f),
                    colors = if (transactionType == TransactionType.EXPENSE) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.outlinedButtonColors(),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text("Keluar", color = if (transactionType == TransactionType.EXPENSE) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.error)
                }
                OutlinedButton(
                    onClick = { transactionType = TransactionType.INCOME },
                    modifier = Modifier.weight(1f),
                    colors = if (transactionType == TransactionType.INCOME) ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)) else ButtonDefaults.outlinedButtonColors(),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text("Masuk", color = if (transactionType == TransactionType.INCOME) Color.White else Color(0xFF2E7D32))
                }
                OutlinedButton(
                    onClick = { transactionType = TransactionType.TRANSFER },
                    modifier = Modifier.weight(1f),
                    colors = if (transactionType == TransactionType.TRANSFER) ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)) else ButtonDefaults.outlinedButtonColors(),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text("Transfer", color = if (transactionType == TransactionType.TRANSFER) Color.White else Color(0xFF1565C0))
                }
            }

            // LOGIKA TAMPILAN BERDASARKAN TIPE
            if (transactionType == TransactionType.TRANSFER) {
                // --- MODE TRANSFER ---
                // Dropdown Dari Akun Sumber
                ExposedDropdownMenuBox(
                    expanded = expandedFromAccountMenu,
                    onExpandedChange = { expandedFromAccountMenu = !expandedFromAccountMenu }
                ) {
                    val fromAccount = accounts.find { it.id == fromAccountId }
                    OutlinedTextField(
                        value = fromAccount?.name ?: "Sumber",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFromAccountMenu) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        label = { Text("Dari Akun") }
                    )
                    ExposedDropdownMenu(expanded = expandedFromAccountMenu, onDismissRequest = { expandedFromAccountMenu = false }) {
                        accounts.forEach { account -> DropdownMenuItem(text = { Text(account.name) }, onClick = { fromAccountId = account.id; expandedFromAccountMenu = false }) }
                    }
                }

                // Dropdown Ke Akun Tujuan
                ExposedDropdownMenuBox(
                    expanded = expandedToAccountMenu,
                    onExpandedChange = { expandedToAccountMenu = !expandedToAccountMenu }
                ) {
                    val toAccount = accounts.find { it.id == toAccountId }
                    OutlinedTextField(
                        value = toAccount?.name ?: "Tujuan",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedToAccountMenu) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        label = { Text("Ke Akun") }
                    )
                    ExposedDropdownMenu(expanded = expandedToAccountMenu, onDismissRequest = { expandedToAccountMenu = false }) {
                        accounts.forEach { account -> DropdownMenuItem(text = { Text(account.name) }, onClick = { toAccountId = account.id; expandedToAccountMenu = false }) }
                    }
                }
            } else {
                // --- MODE PENGELUARAN / PEMASUKAN ---
                // Dropdown Pilih Akun
                ExposedDropdownMenuBox(
                    expanded = expandedAccountMenu,
                    onExpandedChange = { expandedAccountMenu = !expandedAccountMenu }
                ) {
                    val selectedAccount = accounts.find { it.id == selectedAccountId }
                    OutlinedTextField(
                        value = selectedAccount?.name ?: "Pilih Akun",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAccountMenu) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        label = { Text("Akun") }
                    )
                    ExposedDropdownMenu(expanded = expandedAccountMenu, onDismissRequest = { expandedAccountMenu = false }) {
                        accounts.forEach { account -> DropdownMenuItem(text = { Text("${account.name} (${account.type.name})") }, onClick = { selectedAccountId = account.id; expandedAccountMenu = false }) }
                    }
                }

                // Dropdown Pilih Kategori
                ExposedDropdownMenuBox(
                    expanded = expandedCategoryMenu,
                    onExpandedChange = { expandedCategoryMenu = !expandedCategoryMenu }
                ) {
                    val selectedCategory = categories.find { it.id == selectedCategoryId }
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Pilih Kategori",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryMenu) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        label = { Text("Kategori") }
                    )
                    ExposedDropdownMenu(expanded = expandedCategoryMenu, onDismissRequest = { expandedCategoryMenu = false }) {
                        categories.forEach { category -> DropdownMenuItem(text = { Text(category.name) }, onClick = { selectedCategoryId = category.id; expandedCategoryMenu = false }) }
                    }
                }
            }

            // Input Nominal & Catatan (Sama untuk semua tipe)
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Simpan dengan validasi ketat
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    val isValid = if (transactionType == TransactionType.TRANSFER) {
                        amountDouble > 0 && fromAccountId != toAccountId // Akun sumber & tujuan tidak boleh sama
                    } else {
                        amountDouble > 0 && selectedCategoryId != 0
                    }

                    if (isValid) {
                        viewModel.saveTransaction(
                            type = transactionType,
                            amount = amountDouble,
                            note = note,
                            accountId = selectedAccountId,
                            categoryId = selectedCategoryId,
                            fromAccountId = fromAccountId,
                            toAccountId = toAccountId
                        )
                        onSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = amount.isNotBlank() && (transactionType == TransactionType.TRANSFER || selectedCategoryId != 0)
            ) {
                Text("Simpan Transaksi")
            }
        }
    }
}