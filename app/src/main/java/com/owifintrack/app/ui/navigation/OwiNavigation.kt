package com.owifintrack.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.owifintrack.app.ui.dashboard.DashboardScreen
import com.owifintrack.app.ui.add_transaction.AddTransactionScreen
import com.owifintrack.app.ui.history.HistoryScreen

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object AddTransaction : Screen("add_transaction")
    object History : Screen("history")
}

@Composable
fun OwiNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onAddClick = { navController.navigate(Screen.AddTransaction.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            )
        }
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.History.route) {
            HistoryScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
