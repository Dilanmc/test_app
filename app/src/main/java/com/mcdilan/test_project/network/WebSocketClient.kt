package com.mcdilan.test_project.network

import android.util.Log
import com.google.gson.Gson
import com.mcdilan.test_project.BuildConfig
import com.mcdilan.test_project.model.StockInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.*
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor() {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val _data = Channel<StockInfo>(Channel.BUFFERED)
    val dataFlow: Flow<StockInfo> = _data.receiveAsFlow()
    private val event = "realtimeQuotes"


    fun connect() {
        val request = Request.Builder()
            .url(url = BuildConfig.SOCKET_URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.e("Success", "$response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val array = gson.fromJson(text, Array<Any>::class.java)
                val stockInfo = gson.fromJson(gson.toJson(array[1]), StockInfo::class.java)
                _data.trySend(stockInfo)
                Log.e("data", stockInfo.c.toString())
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("Error", "${t.message}")
                reconnect()
            }
        })
    }

    fun getRealtimeQuotes(tickerList: List<String>) {
        webSocket?.send(gson.toJson(listOf(event, tickerList)))
    }

    fun reconnect() {
        Log.e("reconnect", "reconnecting...")
        webSocket?.cancel()
        connect()

    }

    fun close() {
        webSocket?.close(1000, "Goodbye")
        webSocket = null
    }
}