package me.skripsi.data.models

import me.skripsi.data.naiveBayes.labeledPenjualan
import me.skripsi.data.naiveBayes.labeledStok
import me.skripsi.roomdb.entity.DataUjiEntity

data class DataUji(
    val id: Int = 0,
    val kodeBarang: String?,
    val namaBarang: String?,
    val golongan: String?,
    val stok: Int = 0,
    val isDiskon: Boolean,
    val penjualan: Int = 0,
){
    fun toDataUjiEntity(): DataUjiEntity{
        return DataUjiEntity(
            id = id.toLong(),
            kodeBarang = kodeBarang,
            namaBarang = namaBarang,
            golongan = golongan,
            stok = stok,
            isDiskon = isDiskon,
            penjualan = penjualan
        )
    }

    fun toDataUjiCalculate(): DataUjiCalculate {
        return DataUjiCalculate(
            kodeBarang = kodeBarang ?: "",
            namaBarang = namaBarang ?: "",
            kategori = golongan ?: "",
            stok = stok.labeledStok(),
            isDiskon = isDiskon,
            penjualan = penjualan.labeledPenjualan()
        )
    }
}
