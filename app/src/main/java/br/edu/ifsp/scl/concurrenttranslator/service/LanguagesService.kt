package br.edu.ifsp.scl.concurrenttranslator.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import br.edu.ifsp.scl.concurrenttranslator.model.api.DeepTranslateApiClient
import br.edu.ifsp.scl.concurrenttranslator.model.livedata.DeepTranslateLiveData
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

class LanguagesService: Service() {
    private lateinit var handler: LanguagesServiceHandler
    private lateinit var serviceLogTag: String

    private inner class LanguagesServiceHandler(looper: Looper): Handler(looper) {
        override fun handleMessage(msg: Message) {
           DeepTranslateApiClient.service.getLanguages().execute().also {response ->
                if (response.code() == HTTP_OK) {
                    Log.d("API Request", "Successful request")
                    response.body()?.also { languages ->
                        DeepTranslateLiveData.languagesLiveData.postValue(languages)
                    }
                }else{
                    Log.e("API Request", "Failed request with error code: ${response.code()}")
                }
           }
            stopSelf(msg.arg1)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceLogTag = "${this.javaClass.simpleName}/${startId}"
        Log.v(serviceLogTag, "onStartCommand() - Service Started.")

        handler.obtainMessage().also { msg ->
            msg.arg1 = startId
            handler.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(serviceLogTag, "onDestroy() - Service done")
    }

    override fun onCreate() {
        super.onCreate()
        HandlerThread(this.javaClass.name).apply {
            start()
            handler = LanguagesServiceHandler(looper)
        }
    }
    override fun onBind(intent: Intent?): IBinder? = null
}