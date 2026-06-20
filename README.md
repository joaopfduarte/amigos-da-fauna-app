# Amigos da Fauna — Android Nativo

Aplicativo Android nativo desenvolvido com **Kotlin**, **Jetpack Compose** e **MVVM**, portando o app React Native *Amigos da Fauna* para Android nativo.

## Sobre o app

Projeto de educação ambiental sobre a biodiversidade da Mata Atlântica:

- Catálogo de animais com busca e paginação
- Mapa interativo com localização dos animais
- Quizzes educativos por animal
- Autenticação de usuários
- Compartilhamento de informações sobre animais
- Tema claro/escuro/sistema

## Stack

- Kotlin + Jetpack Compose + Material 3
- MVVM + Repository Pattern
- Hilt (injeção de dependências)
- Navigation Compose
- Retrofit + kotlinx-serialization
- DataStore + EncryptedSharedPreferences
- Coil (imagens)
- Google Maps Compose + Fused Location Provider

## API

Backend: `https://api-dm-69db35e2f2d0.herokuapp.com`

## Como executar

1. Abra o projeto no Android Studio
2. Configure a chave do Google Maps em `local.properties`:

```properties
MAPS_API_KEY=sua_chave_aqui
```

3. Sincronize o Gradle e execute no emulador ou dispositivo (minSdk 24)

```bash
./gradlew assembleDebug
```

## Estrutura

```
app/src/main/java/com/app/amigos_da_fauna/
├── data/          # remote, local, repository
├── domain/        # models, repository interfaces
├── ui/            # screens, navigation, theme, components
├── di/            # módulos Hilt
└── util/          # share, location, merge
```

## Deep links

Compartilhamento usa o esquema `amigosdafauna://animals/{id}`.

## Origem

Migrado a partir de [DM-React-Native](https://github.com/joaopfduarte/DM-React-Native).
