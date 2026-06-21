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

## Build Variants

O projeto usa product flavors para separar desenvolvimento com mocks da API real:

| Variante | API backend | Application ID |
|---|---|---|
| `devDebug` / `devRelease` | Mock local (`MockFaunaApi`) | `com.app.amigos_da_fauna.dev` |
| `prodDebug` / `prodRelease` | API Heroku real | `com.app.amigos_da_fauna` |

### Desenvolvimento com mocks

```bash
./gradlew installDevDebug
```

Credenciais de teste do mock:

- **Email:** `dev@test.com`
- **Senha:** `123456`

O flavor `dev` simula paginação, autenticação e quizzes sem conexão com o backend. **Google Maps e localização do dispositivo continuam usando as APIs reais do Google** — apenas as chamadas ao backend Heroku são mockadas.

> **Google Maps:** o flavor `dev` usa o package `com.app.amigos_da_fauna.dev`. Se a chave Maps tiver restrição por package no Google Cloud Console, adicione esse package às restrições.

### Produção / API real

```bash
./gradlew installProdDebug
```

## Como executar

1. Abra o projeto no Android Studio
2. Configure a chave do Google Maps em `local.properties`:

```properties
MAPS_API_KEY=sua_chave_aqui
```

3. Selecione a variante desejada (`devDebug` ou `prodDebug`) e execute no emulador ou dispositivo (minSdk 24)

```bash
./gradlew assembleDevDebug   # mocks
./gradlew assembleProdDebug  # API real
```

## Estrutura

```
app/src/main/java/com/app/amigos_da_fauna/
├── data/          # remote, local, repository
│   └── remote/mock/   # MockFaunaApi e fixtures (dev)
├── domain/        # models, repository interfaces
├── ui/            # screens, navigation, theme, components
├── di/            # módulos Hilt (main)
└── util/          # share, location, merge

app/src/dev/       # DevNetworkModule — injeta MockFaunaApi
app/src/prod/      # ProdNetworkModule — injeta RetrofitFaunaApi
```

## Deep links

Compartilhamento usa o esquema `amigosdafauna://animals/{id}`.

## Origem

Migrado a partir de [DM-React-Native](https://github.com/joaopfduarte/DM-React-Native).
