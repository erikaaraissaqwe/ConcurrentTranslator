package br.edu.ifsp.scl.concurrenttranslator.model.domain

data class Languages (
    val languages: List<Language>
)

data class Language (
    val name: String,
    val language: String
)
