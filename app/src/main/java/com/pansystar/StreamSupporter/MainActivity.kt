package com.pansystar.StreamSupporter

import CustomWebSocketServer
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.pansystar.StreamSupporter.ui.main.SectionsPagerAdapter
import com.pansystar.StreamSupporter.databinding.ActivityMainBinding
import org.hjson.JsonObject
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.InetAddress
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var server: CustomWebSocketServer

    fun getIPAddress():String{
        var text = "";
        try{
            val interfaces = NetworkInterface.getNetworkInterfaces();
            if(interfaces == null){
                text = ""
                return text
            }
            while(interfaces.hasMoreElements()){
                val network = interfaces.nextElement()
                val addresses = network.inetAddresses

                while(addresses.hasMoreElements()){
                    val address = addresses.nextElement()
                    if(address.hostAddress.startsWith("192.168")) {
                        text = address.hostAddress
                        return text
                    }
                }
            }
        }catch (e : SocketException){
            e.printStackTrace()
        }
        return text;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            var json = JsonObject()
            json["type"] = 0x00000001
            json["message"] = findViewById<EditText>(R.id.voiceMessageEditText).text.toString()
            server.sendMessage(json)
        }
    }

    override fun onResume() {
        super.onResume()

        val ipAddress: String = getIPAddress()
        val port = 8888

        if(ipAddress.isNotEmpty()) {

            ShareData.serverIpAddress = ipAddress
            ShareData.serverPort = port

            server = CustomWebSocketServer(InetSocketAddress(ipAddress, port), this)

            server.start()
        } else {
            findViewById<TextView>(R.id.logTextView).text = "鯖の作成に失敗しました。"
        }
    }
}