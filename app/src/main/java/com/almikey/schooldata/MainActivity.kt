package com.almikey.schooldata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.almikey.schooldata.service.MySchoolServerService
import io.reactivex.internal.operators.completable.CompletableFromAction
import io.reactivex.schedulers.Schedulers
import org.zeromq.ZMQ
import org.zeromq.czmq.Zmsg
import org.zeromq.zyre.Zyre

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun startMyService(view: View) {
        val intent = Intent(this, MySchoolServerService::class.java)
        startService(intent)
    }

    fun stopMyService(view: View) {
        val intent = Intent(this, MySchoolServerService::class.java)
        stopService(intent)
    }

    fun sendToServer(view: View) {
//        CompletableFromAction {
//            val context = ZMQ.context(1)
//
//            val socket = context.socket(ZMQ.REQ)
//            Log.d("client send", "connecting to hello world server...")
//            socket.connect("tcp://localhost:5897")
//
//            for (i in 1..10) {
//                var plainRequest = "Hello "
//
//                var byteRequest = plainRequest.toByteArray()
//
//                byteRequest[byteRequest.size - 1] = 0
//
//                Log.d("client send", "sending request $i $plainRequest")
//                socket.send(byteRequest, 0)
//
//                val byteReply = socket.recv(0)
//
//                var plainReply = String(byteReply, 0, byteReply.size - 1)
//
//                Log.d("client send", "Received reply $plainReply")
//
//            }
//        }.subscribeOn(Schedulers.io()).subscribe()
        CompletableFromAction{
            var node = Zyre("Mike")
            node.setHeader("description", "peer on host apple")
            node.setHeader("hostname", "apple")
            node.start()
            node.whispers("Service Mike","hello service it's activity" )
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}
