package br.edu.ifsp.scl.concurrenttranslator.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import br.edu.ifsp.scl.concurrenttranslator.model.api.DeepTranslateApiClient
import br.edu.ifsp.scl.concurrenttranslator.model.domain.TranslateRequest
import br.edu.ifsp.scl.concurrenttranslator.model.livedata.DeepTranslateLiveData
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

class TranslationService: Service() {

    // POST https://deep-translate1.p.rapidapi.com/language/translate/v2

    private lateinit var translationServiceHandler: TranslationServiceHandler

    private val translationServiceBinder = TranslationServiceBinder()
    inner class TranslationServiceBinder : Binder() {
        fun getTranslationService() = this@TranslationService
    }
    private inner class TranslationServiceHandler(looper: Looper): Handler(looper) {
        override fun handleMessage(msg: Message) {

           with (msg.data){
               var translationRequest = TranslateRequest(getString("text", "")!!, getString("source", "")!!, getString("target", "")!!)
               Log.v(this.javaClass.simpleName, "handleMessage() - Translation request: $translationRequest")
               DeepTranslateApiClient.service.getTranslation(translationRequest).execute().also { response ->
                   if (response.code() == HTTP_OK) {
                       Log.v(this.javaClass.simpleName, "handleMessage() - Translation result: ${response.body()}")
                       response.body()?.let { translationResult ->
                           DeepTranslateLiveData.translationLiveData.postValue(translationResult)
                           Log.v(this.javaClass.simpleName, "handleMessage() - Translation result: ${translationResult}")
                       }
                   }
               }
           }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.v(this.javaClass.simpleName, "onBind() - Service Binded.")
        return translationServiceBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    fun translate(text: String, source: String, target: String) {
        Log.v(this.javaClass.simpleName, "translate() - Translating text: $text from $source to $target")
        HandlerThread(this.javaClass.name).apply {
            start()
            translationServiceHandler = TranslationServiceHandler(looper).apply {
                obtainMessage().also { msg ->
                    msg.data.putString("text", text)
                    msg.data.putString("source", source)
                    msg.data.putString("target", target)
                    sendMessage(msg)
                }
            }
        }
    }
}