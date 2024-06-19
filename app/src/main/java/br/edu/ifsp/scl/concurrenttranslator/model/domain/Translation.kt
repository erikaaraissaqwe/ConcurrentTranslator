package br.edu.ifsp.scl.concurrenttranslator.model.domain

data class Translation (
    val data: Data
)

data class Data (
    val translations: Translations
)

data class Translations (
    val translatedText: String
)

data class TranslateRequest(
    val q: String,
    val source: String,
    val target: String
)
