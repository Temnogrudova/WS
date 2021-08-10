package com.example.websocketproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.websocketproject.databinding.ActivityMainBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mWebSocketClient: WebSocketClient? = null
    private var items: ArrayList<ItemModel?> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        connectWebSocket()
    }

    private fun initRecyclerView() {
//
//        var i1 = ItemModel(price = "3")
//        var i2 = ItemModel(price = "32")
//        var i3 = ItemModel(price = "33")
//        var i4 = ItemModel(price = "53")
//        items.add(i1)
//        items.add(i2)
//        items.add(i3)
//        items.add(i4)
        var context  = this
        binding.rvItems.apply {
            binding.rvItems.setHasFixedSize(true)
            binding.rvItems.layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            binding.rvItems.adapter = ItemsListAdapter(
                items
            )
        }
    }

    private fun connectWebSocket() {
        val uri: URI
        try {
            uri = URI("wss://ws-feed.pro.coinbase.com")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        mWebSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                Log.i("Websocket", "Opened")
                //mWebSocketClient!!.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL)
                fetchItems()
            }

            override fun onMessage(response: String) {
                Log.e("Websocket", response)
                updateUI(response)
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.i("Websocket", "Closed $s")
                unregister()

            }

            override fun onError(e: Exception) {
                Log.i("Websocket", "Error " + e.message)
            }
        }
        mWebSocketClient?.connect()
    }

    private fun fetchItems() {
        mWebSocketClient?.send(
            "{\n" +
                    "    \"type\": \"subscribe\",\n" +
                    "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [\"BTC-EUR\"] }]\n" +
                    "}"
        )
    }

    private fun unregister() {
        mWebSocketClient?.send(
            "{\n" +
                    "    \"type\": \"unsubscribe\",\n" +
                    "}"
        )
    }
    override fun onPause() {
        super.onPause()
        mWebSocketClient?.close()
    }

    private fun updateUI(response: String?) {
        response?.let {
            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<ItemModel> = moshi.adapter(ItemModel::class.java)
            val bitcoin = adapter.fromJson(response)
             items.add(bitcoin)
            runOnUiThread {
                (binding.rvItems.adapter as ItemsListAdapter).notifyDataSetChanged()
            }
//            runOnUiThread {
//                (binding.rvItems.adapter as ItemsListAdapter).setData(bitcoin)
//            }
        }
    }
}
