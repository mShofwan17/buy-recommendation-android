package me.skripsi.rekomendasibeliapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import me.skripsi.rekomendasibeliapp.screens.beranda.BerandaScreen
import me.skripsi.rekomendasibeliapp.screens.detail_hasil_uji.DetailHasilUjiScreen
import me.skripsi.rekomendasibeliapp.screens.form_uji.FormUjiScreen
import me.skripsi.rekomendasibeliapp.screens.form_uji.ProductSelectedScreen
import me.skripsi.rekomendasibeliapp.screens.hasil_uji.HasilUjiScreen
import me.skripsi.rekomendasibeliapp.screens.list_data.ListDataScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Beranda.route) {
        composable(route = Screens.Beranda.route) {
            BerandaScreen(navHostController = navController)
        }
        composable(
            route = Screens.ListData.route,
            arguments = listOf(navArgument(name = "isFromTransaksi") {
                type = NavType.BoolType
            })
        ) { navBackStackEntry ->
            ListDataScreen(
                isFromTransaksi = navBackStackEntry.arguments?.getBoolean("isFromTransaksi"),
                navController = navController
            )
        }
        composable(route = Screens.ProductSelected.route) {
            ProductSelectedScreen(navHostController = navController)
        }
        composable(route = Screens.FormUji.route) {
            FormUjiScreen(navHostController = navController)
        }
        composable(
            route = Screens.HasilUji.route,
            arguments = listOf(navArgument(name = "isFromHome") {
                type = NavType.BoolType
            })
        ) {
            it.arguments?.getBoolean("isFromHome")?.let { it1 ->
                HasilUjiScreen(
                    navHostController = navController,
                    isFromHome = it1
                )
            }
        }
        composable(
            route = Screens.DetailHasilUji.route,
            arguments = listOf(
                navArgument(name = "kodeBarang") { type = NavType.StringType }
            )
        ) {
            DetailHasilUjiScreen(navController = navController)
        }
    }
}