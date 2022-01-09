import java.net.InetSocketAddress

object ShareData {
    public var serverIpAddress: String = "127.0.0.1"
        get() {
            return field
        }
        set(value) {
            field = value
        }

    public var serverPort: Int = 0
        get() {
            return field
        }
        set (value) {
            field = value
        }
    fun init() {
    }

    fun read()  {
        println("read")
    }
}