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
import androidx.compose.runtime.LaunchedEffect
import com.craigmurphy.itemlog.session.AuthState
import com.craigmurphy.itemlog.ui.screens.SplashScreen
import com.craigmurphy.itemlog.ui.screens.ExportCsvScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = viewModel()

    LaunchedEffect(sessionViewModel.authState.value) {
        if (sessionViewModel.authState.value is AuthState.Unauthenticated) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH

    ) {composable(Routes.SPLASH) {
        SplashScreen()

        LaunchedEffect(Unit) {
            if (sessionViewModel.isLoggedIn()) {
                navController.navigate(Routes.EVENTS) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }
    }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    sessionViewModel.onLoginSuccess()
                    navController.navigate(Routes.EVENTS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
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
            val eventsMessage =
                backStackEntry.savedStateHandle.get<String>("events_message")

            EventsScreen(
                refreshTrigger = refreshEvents,
                message = eventsMessage,
                onMessageShown = {
                    backStackEntry.savedStateHandle["events_message"] = null
                },
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
                },
                onSessionExpired = {
                    sessionViewModel.handleUnauthorized()
                }
            )
        }

        composable(Routes.CREATE_EVENT) {
            CreateEventScreen(
                onSaveClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh_events", true)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("events_message", "Event created successfully.")
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
            val itemsMessage =
                backStackEntry.savedStateHandle.get<String>("items_message")

            ItemsScreen(
                eventId = eventId,
                refreshTrigger = refreshItems,
                message = itemsMessage,
                onMessageShown = {
                    backStackEntry.savedStateHandle["items_message"] = null
                },
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
                },
                onExportCsvClick = { id ->
                    backStackEntry.savedStateHandle["refresh_items"] = false
                    navController.navigate(Routes.exportCsvRoute(id))
                },
                onItemDeleted = {
                    backStackEntry.savedStateHandle["refresh_items"] = true
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
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("items_message", "Item added successfully.")
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
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("items_message", "Sale recorded successfully.")
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

        composable(
            route = Routes.EXPORT_CSV,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L

            ExportCsvScreen(
                eventId = eventId
            )
        }
    }
}