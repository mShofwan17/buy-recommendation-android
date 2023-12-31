package me.skripsi.domain.usecases.form_uji

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.skripsi.data.repository.form_uji.FormUjiRepository
import me.skripsi.domain.ui_models.UiDataUji
import javax.inject.Inject

class UpdateDataUjiUseCase @Inject constructor(
    private val repository: FormUjiRepository
) {
    operator fun invoke(items: List<UiDataUji>) : Flow<Boolean> {
        return flow {
            try {
                val result = repository.updateDataUji(items.map { it.toDataUji() })
                emit(result)
            }catch (e: Exception){
                Log.i("TAG_Exception", "invoke: ${e.message}")
            }

        }.flowOn(Dispatchers.IO)
    }
}