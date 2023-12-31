package me.skripsi.rekomendasibeliapp.navigation

import me.skripsi.rekomendasibeliapp.utils.constant.ScreenConst.BERANDA
import me.skripsi.rekomendasibeliapp.utils.constant.ScreenConst.DETAIL_HASIL_UJI
import me.skripsi.rekomendasibeliapp.utils.constant.ScreenConst.FORM_UJI
import me.skripsi.rekomendasibeliapp.utils.constant.ScreenConst.HASIL_UJI
import me.skripsi.rekomendasibeliapp.utils.constant.ScreenConst.LIST_DATA
import me.skripsi.rekomendasibeliapp.utils.constant.ScreenConst.PRODUCT_SELECTED

sealed class Screens(val route: String) {
    object Beranda : Screens(BERANDA)
    object DetailHasilUji : Screens("$DETAIL_HASIL_UJI/{kodeBarang}"){
        fun passKodeBarang(kodeBarang: String?): String  {
            return "$DETAIL_HASIL_UJI/$kodeBarang"
        }
    }
    object ProductSelected : Screens(PRODUCT_SELECTED)
    object FormUji : Screens(FORM_UJI)
    object HasilUji : Screens("$HASIL_UJI/{isFromHome}") {
        fun passBoolean(isFromHome: Boolean = true): String {
            return "$HASIL_UJI/$isFromHome"
        }
    }

    object ListData : Screens("$LIST_DATA/{isFromTransaksi}") {
        fun passBoolean(isFromTransaksi: Boolean): String {
            return "$LIST_DATA/$isFromTransaksi"
        }
    }
}
