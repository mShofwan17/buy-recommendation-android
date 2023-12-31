package me.skripsi.rekomendasibeliapp.screens.form_uji

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.skripsi.domain.ui_models.UiDataUji
import me.skripsi.domain.ui_models.UiProductSelected
import me.skripsi.domain.usecases.form_uji.GetAllDataUjiUseCase
import me.skripsi.domain.usecases.form_uji.GetProductSelectedUseCase
import me.skripsi.domain.usecases.form_uji.InsertDataUjiFromCsvUseCase
import me.skripsi.domain.usecases.form_uji.SaveDataUjiUseCase
import me.skripsi.domain.usecases.form_uji.UpdateDataUjiUseCase
import me.skripsi.domain.utils.ResponseState
import me.skripsi.rekomendasibeliapp.ui.UiState
import me.skripsi.rekomendasibeliapp.utils.DataUjiChangedState
import javax.inject.Inject

@HiltViewModel
class FormUjiViewModel @Inject constructor(
    private val getProductSelectedUseCase: GetProductSelectedUseCase,
    private val saveDataUjiUseCase: SaveDataUjiUseCase,
    private val updateDataUjiUseCase: UpdateDataUjiUseCase,
    private val getAllDataUjiUseCase: GetAllDataUjiUseCase,
    private val insertDataUjiFromCsvUseCase: InsertDataUjiFromCsvUseCase
) : ViewModel() {
    private val _productState = MutableStateFlow<UiState<List<UiProductSelected>>>(UiState())
    val productState get() = _productState.asStateFlow()

    private val _selectedProducts = MutableStateFlow<List<UiProductSelected>>(listOf())
    val selectedProducts get() = _selectedProducts.asStateFlow()

    private val _dataUjis = MutableStateFlow<List<UiDataUji>>(listOf())
    val dataUjis: StateFlow<List<UiDataUji>> = _dataUjis

    private val _insertDataUjiFromCsv = MutableStateFlow<UiState<List<UiDataUji>>>(UiState())
    val insertDataUjiFromCsv get() = _insertDataUjiFromCsv.asStateFlow()

    private val _saveToDatabaseState = MutableStateFlow<UiState<Boolean>>(UiState())
    val saveToDatabaseState get() = _saveToDatabaseState.asStateFlow()

    init {
        getProductSelected()
        getAllDataUji()
    }

    private fun getProductSelected() {
        viewModelScope.launch {
            getProductSelectedUseCase().collectLatest {
                _productState.update { state ->
                    if (it.isNotEmpty()) {
                        _selectedProducts.value = it
                        state.success(it)
                    } else state.error("Gagal mendapatkan data")
                }
            }
        }
    }

    fun saveSelectedData(items: List<UiDataUji>) {
        viewModelScope.launch {
            saveDataUjiUseCase(items).collectLatest {
                _saveToDatabaseState.update { state ->
                    when (it) {
                        is ResponseState.Loading -> state.loading()
                        is ResponseState.Success -> {
                            it.data?.let { uiDataUjis ->
                                Log.i("TAG_dataUji", "saveSelectedData: ${uiDataUjis.size}")
                                _dataUjis.update { uiDataUjis }
                            }
                            state.success(true)
                        }
                        is ResponseState.Error -> state.error(message = it.message)
                    }
                }
            }
        }
    }

    fun updateDataUji(items: List<UiDataUji>) {
        viewModelScope.launch {
            updateDataUjiUseCase(items).collectLatest {}
        }
    }

    private fun getAllDataUji() {
        viewModelScope.launch {
            getAllDataUjiUseCase().collectLatest {
                _dataUjis.value = it
            }
        }
    }

    fun insertDataUjiFromCsv(filePath: String) {
        viewModelScope.launch {
            insertDataUjiFromCsvUseCase(filePath).collectLatest {
                _insertDataUjiFromCsv.update { state ->
                    when (it) {
                        is ResponseState.Loading -> state.loading()
                        is ResponseState.Success -> state.success(it.data)
                        is ResponseState.Error -> state.error(it.message)
                        else -> UiState()
                    }
                }
            }
        }
    }

    fun updateSelectedProduct(kodeBarang: String?, isSelected: Boolean) {
        _selectedProducts.value = _selectedProducts.value.map {
            if (it.kodeBarang == kodeBarang) it.copy(isSelected = isSelected)
            else it
        }
    }

    fun updateChangeOnDataUji(kodeBarang: String?, stateChange: DataUjiChangedState) {
        _dataUjis.value = _dataUjis.value.map {
            if (it.kodeBarang == kodeBarang) {
                when (stateChange) {
                    is DataUjiChangedState.Stok -> it.copy(stok = stateChange.updatedValue)
                    is DataUjiChangedState.Diskon -> it.copy(isDiskon = stateChange.updatedValue)
                    is DataUjiChangedState.Penjualan -> it.copy(penjualan = stateChange.updatedValue)
                }
            } else it
        }
    }

    fun resetStateInsertCsv() {
        _insertDataUjiFromCsv.update {
            UiState()
        }
        _saveToDatabaseState.update {
            it.success(false)
        }
    }

    fun resetResultCsv() {
        _insertDataUjiFromCsv.update { UiState() }
    }

    fun isValidateForm(dataUji: List<UiDataUji>): Boolean {
        return dataUji.all { it.penjualan != 0 && it.stok != 0 }
    }
}
