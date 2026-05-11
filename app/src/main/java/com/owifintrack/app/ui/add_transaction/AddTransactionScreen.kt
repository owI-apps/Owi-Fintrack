package com.owifintrack.app.ui.add_transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddTransactionScreen(onBackClick: () -> Unit) {
    // State sementara untuk menyimpan input dari form
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("EXPENSE") }

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
            // Pilihan Tipe: Pemasukan atau Pengeluaran
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { selectedType = "EXPENSE" },
                    modifier = Modifier.weight(1f),
                    colors = if (selectedType == "EXPENSE") ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Pengeluaran", color = if (selectedType == "EXPENSE") MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.error)
                }
                OutlinedButton(
                    onClick = { selectedType = "INCOME" },
                    modifier = Modifier.weight(1f),
                    colors = if (selectedType == "INCOME") ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)) else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Pemasukan", color = if (selectedType == "INCOME") Color.White else Color(0xFF2E7D32))
                }
            }

            // Input Judul Transaksi
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Transaksi") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Input Nominal Uang (Hanya angka)
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Nominal (Rp)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Simpan (Sementara hanya untuk tampilan)
            Button(
                onClick = { /* Nanti kita isi fungsi simpan ke database */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = title.isNotBlank() && amount.isNotBlank()
            ) {
                Text("Simpan Transaksi")
            }
        }
    }
}