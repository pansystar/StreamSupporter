package com.pansystar.StreamSupporter

import CustomWebSocketServer
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.pansystar.StreamSupporter.ui.main.SectionsPagerAdapter
import com.pansystar.StreamSupporter.databinding.ActivityMainBinding
import com.pansystar.StreamSupporter.ui.main.SimpleRecognizerListener
import com.pansystar.StreamSupporter.ui.main.VoiceRecognize
import org.hjson.JsonObject
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.InetAddress
import java.util.*


class MainActivity : AppCompatActivity(), SimpleRecognizerListener.SimpleRecognizerResponseListener {

    companion object {
        internal const val SETTINGS: String = "settings"
        internal const val LOCALE_LANGUAGE: String = "localeLanguage"
    }

    private lateinit var binding: ActivityMainBinding

    lateinit var server: CustomWebSocketServer

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    private fun getIPAddress():String{
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
        getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().apply {
            putString(LOCALE_LANGUAGE, "jp")
            apply()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

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

        VoiceRecognize.init(this)
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

    override fun onDestroy() {

        server.stop()

        super.onDestroy()
    }

    override fun onError(p0: Int) {
        var reason = ""
        when (p0) {
            SpeechRecognizer.ERROR_AUDIO -> reason = "ERROR_AUDIO"
            SpeechRecognizer.ERROR_CLIENT -> reason = "ERROR_CLIENT"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> reason = "ERROR_INSUFFICIENT_PERMISSIONS"
            SpeechRecognizer.ERROR_NETWORK -> reason = "ERROR_NETWORK"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> reason = "ERROR_NETWORK_TIMEOUT"
            SpeechRecognizer.ERROR_NO_MATCH -> reason = "ERROR_NO_MATCH"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> reason = "ERROR_RECOGNIZER_BUSY"
            SpeechRecognizer.ERROR_SERVER -> reason = "ERROR_SERVER"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> reason = "ERROR_SPEECH_TIMEOUT"
        }
//        findViewById<TextView>(R.id.logTextView).text = "Error [" +reason + "]"
    }

    override fun onPartialResults(speechText: String) {
        findViewById<EditText>(R.id.voiceMessageEditText).setText(speechText)
        var json = JsonObject()
        json["type"] = 0x00010001
        json["message"] = speechText
        server.sendMessage(json)
    }

    override fun onResultsResponse(speechText: String) {
//        findViewById<EditText>(R.id.voiceMessageEditText).setText(speechText)
//        var json = JsonObject()
//        json["type"] = 0x00010001
//        json["message"] = speechText
//        server.sendMessage(json)
    }
}