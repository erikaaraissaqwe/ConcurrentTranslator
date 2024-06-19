package br.edu.ifsp.scl.concurrenttranslator.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.edu.ifsp.scl.concurrenttranslator.R
import br.edu.ifsp.scl.concurrenttranslator.databinding.ActivityMainBinding
import br.edu.ifsp.scl.concurrenttranslator.model.livedata.DeepTranslateLiveData
import br.edu.ifsp.scl.concurrenttranslator.service.LanguagesService
import br.edu.ifsp.scl.concurrenttranslator.service.TranslationService

class MainActivity : AppCompatActivity() {

    private val languageMap = mutableMapOf<String, String>()

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val languagensServiceIntent by lazy {
        Intent(this, LanguagesService::class.java)
    }
    private var translationService: TranslationService? = null

    private val translationServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            translationService = (service as TranslationService.TranslationServiceBinder).getTranslationService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            translationService = null
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.mainTb.apply { title = getString(R.string.app_name); subtitle = getString(R.string.subTitle)})

        var fromLanguage = ""
        var toLanguage = ""
        val languagensAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())

        with(amb){
            fromLangMactv.apply {
                setAdapter(languagensAdapter)
                setOnItemClickListener { _, _, _, _ ->
                    fromLanguage = languageMap[text.toString()] ?: ""
                }
            }
            toLangMactv.apply {
                setAdapter(languagensAdapter)
                setOnItemClickListener { _, _, _, _ ->
                    toLanguage = languageMap[text.toString()] ?: ""
                }
            }

            translateBt.setOnClickListener{
                Log.v(this.javaClass.simpleName, "onCreate() - Translating text: ${inputTextEt.text} from $fromLanguage to $toLanguage")
                translationService?.translate(inputTextEt.text.toString(), fromLanguage, toLanguage)
            }
        }

        DeepTranslateLiveData.languagesLiveData.observe(this) { languages ->
            languagensAdapter.clear()
            languages.languages.forEach { language ->
                languageMap[language.name] = language.language
                languagensAdapter.add(language.name)
            }
            languagensAdapter.getItem(0)?.also {
                amb.fromLangMactv.setText(it, false)
                fromLanguage = languageMap[it] ?: ""
            }
            languagensAdapter.getItem(10)?.also {
                amb.toLangMactv.setText(it, false)
                toLanguage = languageMap[it] ?: ""
            }
        }

        DeepTranslateLiveData.translationLiveData.observe(this){ translationResult ->
            with(amb){
                Log.v(this.javaClass.simpleName, "onCreate() - Translated text: $translationResult")
                translationResult.data.translations.translatedText.also {
                    outputTextTiet.setText(it, android.widget.TextView.BufferType.EDITABLE)
                }

                outputTextTiet.visibility = android.view.View.VISIBLE
                outputTextTil.visibility = android.view.View.VISIBLE
            }
        }

        DeepTranslateLiveData.errorLiveData.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

        swipeRefreshLayout.setOnRefreshListener {
            with(amb){
                inputTextEt.text.clear()
                outputTextTil.visibility = View.GONE
                outputTextTiet.visibility = View.GONE
                fromLangMactv.text.clear()
                toLangMactv.text.clear()
            }
            stopService(languagensServiceIntent)
            startService(languagensServiceIntent)

            swipeRefreshLayout.isRefreshing = false
        }

        startService(languagensServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(languagensServiceIntent)
    }

    override fun onStart() {
        super.onStart()
        Intent(this@MainActivity, TranslationService::class.java).also { intent ->
            bindService(intent, translationServiceConnection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(translationServiceConnection)
    }
}