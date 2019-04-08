package com.almikey.schooldata.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import io.reactivex.internal.operators.completable.CompletableFromAction
import io.reactivex.schedulers.Schedulers
import org.zeromq.ZMQ
import org.zeromq.zyre.Zyre

class MySchoolServerService : Service() {

    inner class SimpleThread : Thread() {
        public override fun run() {
            val context = ZMQ.context(1)

            val socket = context.socket(ZMQ.REP)
            Log.d("server send","Starting the server...")

            socket.bind("tcp://*:5897")
            val wifiMan = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInf = wifiMan.connectionInfo
            val ipAddress = wifiInf.ipAddress
            val ip = String.format(
                "%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )
            val mask = String.format(
                "%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff
            )
            Log.i("school server ip :",ip)

            while (true) {

                val rawRequest = socket.recv(0)

                val cleanRequest = String(rawRequest, 0, rawRequest.size - 1)
                Log.d("server send","Request received : $cleanRequest")

                var plainReply = "World "

                var rawReply = plainReply.toByteArray()

                rawReply[rawReply.size - 1] = 0

                socket.send(rawReply, 0)
            }
        }
    }
    var newThread = SimpleThread()

    override fun onCreate() {
//        newThread.start()
        CompletableFromAction{
            var node = Zyre("Service Mike")
            node.setHeader("description", "service peer on host apple")
            node.setHeader("hostname", "service apple")
            node.start()
            var hostname = node.peerHeaderValue("Mike","hostname")
            Log.d("hostname","the host name is $hostname")
            while(true){
                var theRes = node.recv()
                Log.d("service msg",theRes.popstr())

            }
        }.subscribeOn(Schedulers.io()).subscribe()
        Log.i("MyService", "onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MyService", "onStartCommand() called")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.i("MyService", "Destroyer of worldss!!")
        newThread.stop()
        super.onDestroy()
    }
}
