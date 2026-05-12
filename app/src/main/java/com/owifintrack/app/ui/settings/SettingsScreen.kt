package com.owifintrack.app.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var isDarkMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan & Profil") },
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
            // KARTU PROFIL PENGGUNA
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profil",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Pengguna Owi", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Brad.fintrack@email.com", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    }
                }
            }

            // DAFTAR PENGATURAN
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    // Tema
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsItem(icon = Icons.Default.DarkMode, title = "Mode Gelap (Dark Mode)")
                        Switch(checked = isDarkMode, onCheckedChange = { isDarkMode = it })
                    }
                    HorizontalDivider()

                    // Bahasa (BARU)
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth().clickable { /* Nanti diisi logika ganti bahasa */ },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsItem(icon = Icons.Default.Language, title = "Bahasa (Language)")
                        Text("Indonesia", color = MaterialTheme.colorScheme.primary)
                    }
                    HorizontalDivider()

                    // Mata Uang
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth().clickable { },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsItem(icon = Icons.Default.AttachMoney, title = "Mata Uang")
                        Text("IDR (Rp)", color = MaterialTheme.colorScheme.primary)
                    }
                    HorizontalDivider()

                    // Keamanan
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth().clickable { },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsItem(icon = Icons.Default.Lock, title = "Keamanan (PIN / Sidik Jari)")
                        Icon(Icons.Default.ChevronRight, contentDescription = "Masuk")
                    }
                    HorizontalDivider()

                    // Backup
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth().clickable { },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsItem(icon = Icons.Default.CloudUpload, title = "Backup & Restore Data")
                        Icon(Icons.Default.ChevronRight, contentDescription = "Masuk")
                    }
                }
            }

            // DUKUNGAN & TENTANG
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    // Dukung Pengembang via Trakteer (BERFUNGSI!)
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth().clickable {
                            // Kode untuk membuka link Trakteer di browser
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://trakteer.id/owi_apps/gift"))
                            context.startActivity(intent)
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsItem(icon = Icons.Default.Favorite, title = "Dukung Pengembang")
                        Icon(Icons.Default.OpenInNew, contentDescription = "Buka Browser")
                    }
                    HorizontalDivider()

                    // Tentang Aplikasi
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth().clickable { },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsItem(icon = Icons.Default.Info, title = "Tentang Owi Fintrack")
                        Icon(Icons.Default.ChevronRight, contentDescription = "Masuk")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge)
    }
}