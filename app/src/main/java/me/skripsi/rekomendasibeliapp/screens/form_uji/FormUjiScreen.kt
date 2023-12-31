package me.skripsi.rekomendasibeliapp.screens.form_uji

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.skripsi.domain.ui_models.UiDataUji
import me.skripsi.rekomendasibeliapp.R
import me.skripsi.rekomendasibeliapp.components.ContentFormAndResult
import me.skripsi.rekomendasibeliapp.components.MyButton
import me.skripsi.rekomendasibeliapp.navigation.Screens
import me.skripsi.rekomendasibeliapp.utils.DataUjiChangedState

@Composable
fun FormUjiScreen(
    navHostController: NavHostController,
    viewModel: FormUjiViewModel = hiltViewModel()
) {
    val dataUjis by viewModel.dataUjis.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ListFormDataUji(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            items = dataUjis,
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.padding(top = 8.dp, end = 8.dp))
        MyButton(
            title = stringResource(id = R.string.uji_data),
            icon = Icons.Default.CheckCircle,
            backgroundColor = Color.Blue
        ) {
            if (viewModel.isValidateForm(dataUjis)){
                scope.launch {
                    async { viewModel.updateDataUji(dataUjis) }.await()
                    navHostController.navigate(Screens.HasilUji.passBoolean(false))
                }
            }else{
                Toast.makeText(context, "Lengkapi Form terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun ListFormDataUji(
    modifier: Modifier = Modifier,
    items: List<UiDataUji>,
    viewModel: FormUjiViewModel
) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = Unit){
        listState.animateScrollToItem(index = 2)
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        items(items.size) {
            var item by remember { mutableStateOf(items[it]) }
            ContentFormAndResult(
                dataUji = item,
                onDataChange = {
                    it.apply {
                        item = when (this) {
                            is DataUjiChangedState.Stok -> item.copy(stok = updatedValue)
                            is DataUjiChangedState.Diskon -> item.copy(isDiskon = updatedValue)
                            is DataUjiChangedState.Penjualan -> item.copy(penjualan = updatedValue)
                        }
                        viewModel.updateChangeOnDataUji(item.kodeBarang, this)
                    }

                }
            )
        }
    }
}