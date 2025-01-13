package com.mcdilan.test_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcdilan.test_project.model.StockInfo
import com.mcdilan.test_project.network.WebSocketClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient
) : ViewModel() {
    private val tickerList = listOf(
        "SP500.IDX",
        "AAPL.US",
        "RSTI",
        "GAZP",
        "MRKZ",
        "RUAL",
        "HYDR",
        "MRKS",
        "SBER",
        "FEES",
        "TGKA",
        "VTBR",
        "ANH.US",
        "VICL.US",
        "BURG.US",
        "NBL.US",
        "YETI.US",
        "WSFS.US",
        "NIO.US",
        "DXC.US",
        "MIC.US",
        "HSBC.US",
        "EXPN.EU",
        "GSK.EU",
        "SHP.EU",
        "MAN.EU",
        "DB1.EU",
        "MUV2.EU",
        "TATE.EU",
        "KGF.EU",
        "MGGT.EU",
        "SGGD.EU"
    )
    private val _data = MutableStateFlow<List<StockInfo>>(emptyList())
    val data = _data.asStateFlow()

    init {
        webSocketClient.connect()
        webSocketClient.getRealtimeQuotes(tickerList)
        collectData()
    }

    private fun collectData() {
        viewModelScope.launch {
            webSocketClient.dataFlow.collect { d ->
                d.c?.let {
                    updateStockData(d)
                }
            }
        }
    }

    fun updateStockData(newStock: StockInfo) {
        _data.update { cl ->
            val index = cl.indexOfFirst { it.c == newStock.c }
            if (index != -1) {
                cl.toMutableList().apply {
                    this[index] = newStock
                }
            } else {
                cl + newStock
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.close()
    }
}