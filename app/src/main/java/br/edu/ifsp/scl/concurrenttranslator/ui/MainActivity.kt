package br.edu.ifsp.scl.concurrenttranslator.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.concurrenttranslator.R
import br.edu.ifsp.scl.concurrenttranslator.databinding.ActivityMainBinding
import br.edu.ifsp.scl.concurrenttranslator.model.livedata.DeepTranslateLiveData
import br.edu.ifsp.scl.concurrenttranslator.service.LanguagesService

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val languagensServiceIntent by lazy {
        Intent(this, LanguagesService::class.java)
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
                    fromLanguage = text.toString()
                }
            }
            toLangMactv.apply {
                setAdapter(languagensAdapter)
                setOnItemClickListener { _, _, _, _ ->
                    toLanguage = text.toString()
                }
            }

            translateBt.setOnClickListener{}
        }

        DeepTranslateLiveData.languagesLiveData.observe(this){ languagens ->
            println("TESTE" + languagens)
            languagens.languages.forEach { println(it) }
            languagensAdapter.clear()
            languagensAdapter.addAll(languagens.languages.map { it.name })
            languagensAdapter.getItem(0)?.also {
                amb.fromLangMactv.setText(it, false)
                println("TESTE" + it)
                fromLanguage = it
            }
            languagensAdapter.getItem(1)?.also {
                amb.toLangMactv.setText(it, false)
                println("TESTE" + it)
                toLanguage = it
            }
        }

        startService(languagensServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(languagensServiceIntent)
    }
}