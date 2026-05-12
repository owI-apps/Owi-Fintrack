package com.owifintrack.app.ui.accounts

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.owifintrack.app.data.model.Account
import com.owifintrack.app.data.model.AccountType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onBackClick: () -> Unit,
    viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val accounts by viewModel.allAccounts.collectAsState()

    // State Form
    var name by remember { mutableStateOf("") }
    var initialBalance by remember { mutableStateOf("") }
    var expandedTypeMenu by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(AccountType.CASH) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Akun Harta") },
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
            // FORM TAMBAH AKUN BARU
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tambah Akun Baru", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nama Akun (Misal: GoPay)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Dropdown Tipe Akun
                        ExposedDropdownMenuBox(
                            expanded = expandedTypeMenu,
                            onExpandedChange = { expandedTypeMenu = !expandedTypeMenu }
                        ) {
                            OutlinedTextField(
                                value = selectedType.name,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTypeMenu) },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                label = { Text("Tipe Akun") }
                            )
                            ExposedDropdownMenu(
                                expanded = expandedTypeMenu,
                                onDismissRequest = { expandedTypeMenu = false }
                            ) {
                                AccountType.values().forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.name) },
                                        onClick = {
                                            selectedType = type
                                            expandedTypeMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = initialBalance,
                            onValueChange = { initialBalance = it },
                            label = { Text("Saldo Awal (Rp)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val balance = initialBalance.toDoubleOrNull() ?: 0.0
                                if (name.isNotBlank()) {
                                    viewModel.saveAccount(name, selectedType, balance)
                                    // Reset form
                                    name = ""
                                    initialBalance = ""
                                    selectedType = AccountType.CASH
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = name.isNotBlank()
                        ) {
                            Text("Simpan Akun")
                        }
                    }
                }
            }

            // DAFTAR AKUN YANG SUDAH ADA
            item {
                Text("Daftar Akun Anda", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (accounts.isEmpty()) {
                item {
                    Text("Belum ada akun. Buat yang pertama!", color = MaterialTheme.colorScheme.error)
                }
            } else {
                items(accounts) { account ->
                    AccountItem(account)
                }
            }
        }
    }
}

@Composable
fun AccountItem(account: Account) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(account.name, style = MaterialTheme.typography.titleMedium)
                Text(account.type.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Text(
                text = "Rp %,.0f".format(account.initialBalance).replace(',', '.'),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}