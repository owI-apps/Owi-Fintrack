package com.owifintrack.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.owifintrack.app.ui.accounts.AccountScreen
import com.owifintrack.app.ui.dashboard.DashboardScreen
import com.owifintrack.app.ui.add_transaction.AddTransactionScreen
import com.owifintrack.app.ui.history.HistoryScreen
import com.owifintrack.app.ui.debt.DebtScreen // Import baru

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object AddTransaction : Screen("add_transaction")
    object History : Screen("history")
    object Accounts : Screen("accounts")
    object Debts : Screen("debts") // Rute baru
}

@Composable
fun OwiNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onAddClick = { navController.navigate(Screen.AddTransaction.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onAccountsClick = { navController.navigate(Screen.Accounts.route) },
                onDebtsClick = { navController.navigate(Screen.Debts.route) } // Navigasi baru
            )
        }
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Accounts.route) {
            AccountScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Debts.route) { // Komponen baru
            DebtScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}