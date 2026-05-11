package com.owifintrack.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owifintrack.app.data.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

// Data Palsu (Dummy) untuk mengisi tampilan sementara
val dummyTransactions = listOf(
    Transaction(id = 1, title = "Gaji Bulanan", amount = 5000000.0, type = "INCOME", category = "Gaji", date = Date()),
    Transaction(id = 2, title = "Makan Siang", amount = 35000.0, type = "EXPENSE", category = "Makanan", date = Date()),
    Transaction(id = 3, title = "Belanja Bulanan", amount = 450000.0, type = "EXPENSE", category = "Kebutuhan", date = Date()),
    Transaction(id = 4, title = "Freelance", amount = 1200000.0, type = "INCOME", category = "Proyek", date = Date())
)

@Composable
fun DashboardScreen(
    onAddClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    // Hitung saldo dari data dummy
    val totalIncome = dummyTransactions.filter { it.type == "INCOME" }.sumOf { it.amount }
    val totalExpense = dummyTransactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    val balance = totalIncome - totalExpense

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Judul Aplikasi
            item {
                Text(
                    text = "Owi Fintrack",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Kartu Saldo Utama
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Saldo Saat Ini", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = "Rp ${"%,.0f".format(balance)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Pemasukan", fontSize = 12.sp, color = Color(0xFF2E7D32))
                                Text(text = "Rp ${"%,.0f".format(totalIncome)}", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Pengeluaran", fontSize = 12.sp, color = Color(0xFFC62828))
                                Text(text = "Rp ${"%,.0f".format(totalExpense)}", fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                            }
                        }
                    }
                }
            }

            // Tombol Lihat Riwayat
            item {
                OutlinedButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.History, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lihat Semua Riwayat")
                }
            }

            // Daftar Transaksi Terakhir
            item {
                Text(text = "Transaksi Terakhir", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            // Menampilkan daftar dummy
            items(dummyTransactions) { transaction ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = transaction.title, fontWeight = FontWeight.SemiBold)
                            Text(text = transaction.category, fontSize = 12.sp, color = Color.Gray)
                        }
                        Text(
                            text = if (transaction.type == "INCOME") "+ Rp ${"%,.0f".format(transaction.amount)}" else "- Rp ${"%,.0f".format(transaction.amount)}",
                            fontWeight = FontWeight.Bold,
                            color = if (transaction.type == "INCOME") Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }
        }
    }
}