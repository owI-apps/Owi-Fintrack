package com.owifintrack.app.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    onAddClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Transaksi")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Owi Fintrack", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Saldo Saat Ini", fontSize = 16.sp)
                    Text(text = "Rp 0", style = MaterialTheme.typography.headlineMedium)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onHistoryClick) {
                Icon(Icons.Default.History, contentDescription = "Riwayat")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lihat Riwayat")
            }
        }
    }
}
