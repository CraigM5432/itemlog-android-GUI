package com.craigmurphy.itemlog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                onRegisterClick = { },
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
                    navController.navigate(Routes.ITEMS)
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

        composable(Routes.ITEMS) {
            ItemsScreen(
                onAddItemClick = {
                    navController.navigate(Routes.ADD_ITEM)
                },
                onRecordSaleClick = {
                    navController.navigate(Routes.RECORD_SALE)
                },
                onTransactionsClick = {
                    navController.navigate(Routes.TRANSACTIONS)
                }
            )
        }

        composable(Routes.ADD_ITEM) {
            AddItemScreen(
                onSaveClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.RECORD_SALE) {
            RecordSaleScreen(
                onSaveClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.TRANSACTIONS) {
            TransactionsScreen()
        }
    }
}