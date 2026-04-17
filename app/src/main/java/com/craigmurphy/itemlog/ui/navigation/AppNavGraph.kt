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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.viewmodel.SessionViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = viewModel()

    val startDestination = if (sessionViewModel.isLoggedIn()) {
        Routes.EVENTS
    } else {
        Routes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
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

        composable(Routes.EVENTS) { backStackEntry ->
            val refreshEvents =
                backStackEntry.savedStateHandle.get<Boolean>("refresh_events") ?: false

            EventsScreen(
                refreshTrigger = refreshEvents,
                onCreateEventClick = {
                    backStackEntry.savedStateHandle["refresh_events"] = false
                    navController.navigate(Routes.CREATE_EVENT)
                },
                onEventClick = { eventId ->
                    backStackEntry.savedStateHandle["refresh_events"] = false
                    navController.navigate(Routes.itemsRoute(eventId))
                },
                onLogoutClick = {
                    sessionViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CREATE_EVENT) {
            CreateEventScreen(
                onSaveClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh_events", true)
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
            val refreshItems =
                backStackEntry.savedStateHandle.get<Boolean>("refresh_items") ?: false

            ItemsScreen(
                eventId = eventId,
                refreshTrigger = refreshItems,
                onAddItemClick = { id ->
                    backStackEntry.savedStateHandle["refresh_items"] = false
                    navController.navigate(Routes.addItemRoute(id))
                },
                onRecordSaleClick = { id ->
                    backStackEntry.savedStateHandle["refresh_items"] = false
                    navController.navigate(Routes.recordSaleRoute(id))
                },
                onTransactionsClick = { id ->
                    backStackEntry.savedStateHandle["refresh_items"] = false
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
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh_items", true)
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
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L

            RecordSaleScreen(
                eventId = eventId,
                onSaveClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh_items", true)
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
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L

            TransactionsScreen(
                eventId = eventId
            )
        }
    }
}