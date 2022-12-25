package com.example.loginpage


import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.anilkumawat3104.recyclerview.messages
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.time.LocalDateTime

object SocketHandler  {

    lateinit var mSocket: Socket
    val gson: Gson = Gson()


    @Synchronized
    fun setSocket() {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
            mSocket = IO.socket("http://192.168.43.79:4000")


//            mSocket.on("receive-message"){args ->
//                if(args[0]!=null){
//                    val m=args[0]
//
//                    Log.d("kkk","9")
//                    val mes=gson.fromJson(args[0].toString(), messages::class.java)
//
//                    localmess = localMessages(null,mes.mess,mes.senderid,mes.receiverid,
//                        LocalDateTime.now().toString())
//
////                Log.d("rrr","12")
////                writedata(localmess)
//                    runOnUiThread{
//
//                    }
//                }
//
//            }

        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }


}