package com.example.websocketproject

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.websocketproject.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mWebSocketClient: WebSocketClient? = null
    private var items: ArrayList<GroceryModel?> = arrayListOf()
    private var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBtnsClick()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        connectWebSocket()
    }
    private fun initBtnsClick() {
        binding.btnConnect.setOnClickListener(View.OnClickListener {
            connectWebSocket()

        })
        binding.btnDisconnect.setOnClickListener(View.OnClickListener {
            mWebSocketClient?.close()

        })
    }

    private fun initRecyclerView() {
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
                items,
                adapterInteractionCallbackImpl
            )
        }
    }

    private val adapterInteractionCallbackImpl = object:
            ItemsListAdapter.InteractionListener {
        override fun onItemClick(v: View, grocery: GroceryModel?) {
            grocery?.bagColor?.let { it ->
               Log.d("myLog", "clicked")
               loadFragment(it)
            }
        }
    }

    private fun loadFragment(color: String){
        if (!isConnected) {
            val fragment = SecondFragment()
            val bundle = Bundle()
            bundle.putString("color", color)
            fragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left);
            transaction.add(R.id.container, fragment)
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    private fun connectWebSocket() {
        val uri: URI
        try {
            uri = URI(WS_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        mWebSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                Log.i("Websocket", "Opened")
                isConnected = true
            }

            override fun onMessage(response: String) {
                Log.e("Websocket", response)
                updateUI(response)
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.i("Websocket", "Closed $s")
                isConnected = false

            }

            override fun onError(e: Exception) {
                Log.i("Websocket", "Error " + e.message)
            }
        }
        mWebSocketClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        mWebSocketClient?.close()
    }

    private fun updateUI(response: String?) {
        response?.let {
            val groceryItem = Gson().fromJson<GroceryModel>(response, GroceryModel::class.java)
            items.add(groceryItem)
            runOnUiThread {
                (binding.rvItems.adapter as ItemsListAdapter).notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val WS_URL = "wss://superdo-groceries.herokuapp.com/receive"
    }
}
