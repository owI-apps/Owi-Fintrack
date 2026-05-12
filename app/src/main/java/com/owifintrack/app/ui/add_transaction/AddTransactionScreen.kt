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
import com.owifintrack.app.data.model.Category
import com.owifintrack.app.data.model.CategoryType
import com.owifintrack.app.data.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit // Dipanggil setelah berhasil simpan, untuk kembali ke Dashboard
) {
    val viewModel: AddTransactionViewModel = viewModel(
        factory = AddTransactionViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableIntStateOf(0) }
    var expandedCategoryMenu by remember { mutableStateOf(false) }

    // Tentukan daftar kategori berdasarkan tipe transaksi yang dipilih
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pilihan Tipe
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { 
                        transactionType = TransactionType.EXPENSE 
                        selectedCategoryId = 0 // Reset kategori saat ganti tipe
                    },
                    modifier = Modifier.weight(1f),
                    colors = if (transactionType == TransactionType.EXPENSE) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Pengeluaran", color = if (transactionType == TransactionType.EXPENSE) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.error)
                }
                OutlinedButton(
                    onClick = { 
                        transactionType = TransactionType.INCOME
                        selectedCategoryId = 0 
                    },
                    modifier = Modifier.weight(1f),
                    colors = if (transactionType == TransactionType.INCOME) ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)) else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Pemasukan", color = if (transactionType == TransactionType.INCOME) Color.White else Color(0xFF2E7D32))
                }
            }

            // Input Nominal
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Nominal (Rp)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Input Judul / Catatan
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Catatan (Misal: Makan Siang)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Dropdown Pilih Kategori
            ExposedDropdownMenuBox(
                expanded = expandedCategoryMenu,
                onExpandedChange = { expandedCategoryMenu = !expandedCategoryMenu }
            ) {
                val selectedCategory = categories.find { it.id == selectedCategoryId }
                OutlinedTextField(
                    value = selectedCategory?.name ?: "Pilih Kategori",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryMenu) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    label = { Text("Kategori") }
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoryMenu,
                    onDismissRequest = { expandedCategoryMenu = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategoryId = category.id
                                expandedCategoryMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Simpan
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    if (amountDouble > 0 && selectedCategoryId != 0) {
                        viewModel.saveTransaction(
                            type = transactionType,
                            amount = amountDouble,
                            note = note,
                            accountId = 1, // Sementara hardcode ke Dompet Cash (ID 1)
                            categoryId = selectedCategoryId
                        )
                        onSaved() // Kembali ke dashboard setelah simpan
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = amount.isNotBlank() && selectedCategoryId != 0
            ) {
                Text("Simpan Transaksi")
            }
        }
    }
}