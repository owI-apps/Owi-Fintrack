package com.owifintrack.app.ui.dashboard

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// Fungsi helper untuk format Rupiah yang rapi
fun formatRupiah(amount: Double): String {
    return "Rp %,.0f".format(amount).replace(',', '.')
}

@Composable
fun DashboardScreen(
    onAddClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAccountsClick: () -> Unit,
    onDebtsClick: () -> Unit // Parameter baru untuk Hutang/Piutang
) {
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val netWorth by viewModel.netWorth.collectAsState()
    val totalAssets by viewModel.totalAssets.collectAsState()
    val totalDebt by viewModel.totalDebt.collectAsState()
    val totalReceivable by viewModel.totalReceivable.collectAsState()

    val safeNetWorth = netWorth ?: 0.0
    val safeAssets = totalAssets ?: 0.0
    val safeDebt = totalDebt ?: 0.0
    val safeReceivable = totalReceivable ?: 0.0

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Transaksi")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Owi Fintrack",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Bayangan Keuangan Nyata Anda", 
                style = MaterialTheme.typography.bodyMedium, 
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            val netWorthColor = if (safeNetWorth >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Kekayaan Bersih (Net Worth)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(
                        text = formatRupiah(safeNetWorth),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = netWorthColor
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Total Aset", fontSize = 11.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)
                        Text(text = formatRupiah(safeAssets), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Total Hutang", fontSize = 11.sp, color = Color(0xFFC62828), fontWeight = FontWeight.SemiBold)
                        Text(text = formatRupiah(safeDebt), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Total Piutang (Uang yang harus masuk)", fontSize = 11.sp, color = Color(0xFF1565C0), fontWeight = FontWeight.SemiBold)
                    Text(text = formatRupiah(safeReceivable), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onHistoryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lihat Semua Riwayat Transaksi")
            }

            OutlinedButton(
                onClick = onAccountsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kelola Akun (Bank, E-Wallet, dll)")
            }

            // Tombol Hutang & Piutang (BARU)
            OutlinedButton(
                onClick = onDebtsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kelola Hutang & Piutang")
            }
        }
    }
}