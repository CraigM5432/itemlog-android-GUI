package com.craigmurphy.itemlog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.craigmurphy.itemlog.ui.screens.AddItemScreen
import com.craigmurphy.itemlog.ui.screens.CreateEventScreen
import com.craigmurphy.itemlog.ui.screens.EventsScreen
import com.craigmurphy.itemlog.ui.screens.ItemsScreen
import com.craigmurphy.itemlog.ui.screens.LoginScreen
import com.craigmurphy.itemlog.ui.screens.RecordSaleScreen
import com.craigmurphy.itemlog.ui.screens.RegisterScreen
import com.craigmurphy.itemlog.ui.screens.TransactionsScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Routes.EVENTS)
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.EVENTS) {
            EventsScreen(
                onCreateEventClick = {
                    navController.navigate(Routes.CREATE_EVENT)
                },
                onEventClick = { eventId ->
                    navController.navigate(Routes.itemsRoute(eventId))
                }
            )
        }

        composable(Routes.CREATE_EVENT) {
            CreateEventScreen(
                onSaveClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.ITEMS,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L

            ItemsScreen(
                eventId = eventId,
                onAddItemClick = { id ->
                    navController.navigate(Routes.addItemRoute(id))
                },
                onRecordSaleClick = { id ->
                    navController.navigate(Routes.recordSaleRoute(id))
                },
                onTransactionsClick = { id ->
                    navController.navigate(Routes.transactionsRoute(id))
                }
            )
        }

        composable(
            route = Routes.ADD_ITEM,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L

            AddItemScreen(
                eventId = eventId,
                onSaveClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.RECORD_SALE,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) {
            RecordSaleScreen(
                onSaveClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.TRANSACTIONS,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) {
            TransactionsScreen()
        }
    }
}