import android.util.JsonReader
import android.widget.TextView
import com.pansystar.StreamSupporter.MainActivity
import com.pansystar.StreamSupporter.R
import org.hjson.HjsonDsf
import org.hjson.HjsonOptions
import org.hjson.JsonObject
import org.hjson.JsonValue
import java.net.InetSocketAddress
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.json.JSONObject
import java.lang.Exception
import java.lang.NumberFormatException

class CustomWebSocketServer : WebSocketServer {

    lateinit var mainActivity: MainActivity
    var contextList: ArrayList<WebSocket?> = ArrayList<WebSocket?>()

    lateinit var ipAddress: InetSocketAddress

    public fun restart() {

    }

    public fun sendMessage(json: JsonValue) {
        var list: ArrayList<WebSocket?> = ArrayList<WebSocket?>()

        var message = json.toString()

        contextList.forEach {
            try {
                it?.send(message)
            } catch (e: Exception){
                list.add(it)
            }
        }

        if(list.isNotEmpty()) {
            contextList.forEach{
                contextList.remove(it)
            }
        }
    }

    constructor(address: InetSocketAddress, mainActivity: MainActivity) : super(address) {
        this.mainActivity = mainActivity
        this.ipAddress = address
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {

        var json = JsonObject()
        json["type"] = 0x80000001
        json["message"] = "接続成功"
        conn?.send(json.toString())
        contextList.add(conn)
        mainActivity.findViewById<TextView>(R.id.logTextView).text = "接続されました[from " + conn?.remoteSocketAddress +"]"
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        contextList.forEach { _ ->
            contextList.remove(conn)
        }
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        try {
            var json: JsonObject = JsonValue.readJSON(message).asObject()

        } catch (e: Exception){

        }
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        mainActivity.findViewById<TextView>(R.id.logTextView).text = ex.toString()
    }

    override fun onStart() {
        mainActivity.findViewById<TextView>(R.id.logTextView).text = "鯖を作成しました。[" + ipAddress.hostName + ":" + ipAddress.port + "]"
    }
}