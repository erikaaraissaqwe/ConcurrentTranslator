# Concurrent Translator

Este é um projeto de tradução desenvolvido em Kotlin utilizando Retrofit. O projeto utiliza a API Deep Translate para fornecer funcionalidades de tradução.

## Funcionalidades
- Tradução de texto de uma língua para outra.

## Estrutura do Projeto
O projeto é dividido em duas partes principais: o serviço de idiomas e o serviço de tradução.

- **LanguagesService**: Responsável por obter a lista de idiomas disponíveis para tradução. Ele faz isso chamando a API Deep Translate e postando a resposta em um LiveData.
- **TranslationService**: Responsável por traduzir o texto de um idioma para outro. Ele faz isso chamando a API Deep Translate com um pedido de tradução e postando a resposta em um LiveData.

## Vídeo
https://github.com/erikaaraissaqwe/ConcurrentTranslator/assets/45106624/7c9a6586-449d-4f82-9e4b-e87270289f8e

