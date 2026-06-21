package com.app.amigos_da_fauna.data.remote.mock

import com.app.amigos_da_fauna.data.remote.dto.AnimalDto
import com.app.amigos_da_fauna.data.remote.dto.AnimalLocationDto
import com.app.amigos_da_fauna.data.remote.dto.GeoLocationDto
import com.app.amigos_da_fauna.data.remote.dto.QuizQuestionDto

object MockData {

    const val PAGE_SIZE = 4

    const val MOCK_EMAIL = "dev@test.com"
    const val MOCK_PASSWORD = "123456"
    const val MOCK_ACCESS_TOKEN = "mock-access-token-dev"

    const val DEFAULT_ANIMAL_IMAGE_URL =
        "https://i.ibb.co/nqCdXXX1/images.webp"

    val animals: List<AnimalDto> = listOf(
        AnimalDto(
            id = 1,
            name = "Onça-pintada",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "67–76 cm",
            weight = "56–96 kg",
        ),
        AnimalDto(
            id = 2,
            name = "Mico-leão-dourado",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "20–25 cm",
            weight = "620–794 g",
        ),
        AnimalDto(
            id = 3,
            name = "Tucano-toco",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "55–65 cm",
            weight = "500–876 g",
        ),
        AnimalDto(
            id = 4,
            name = "Arara-azul-grande",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "95–100 cm",
            weight = "1,2–1,7 kg",
        ),
        AnimalDto(
            id = 5,
            name = "Capivara",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "50–62 cm",
            weight = "35–66 kg",
        ),
        AnimalDto(
            id = 6,
            name = "Tamanduá-bandeira",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "1,8–2,2 m",
            weight = "33–41 kg",
        ),
        AnimalDto(
            id = 7,
            name = "Quati",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "42–67 cm",
            weight = "2–7 kg",
        ),
        AnimalDto(
            id = 8,
            name = "Preguiça-de-três-dedos",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            height = "45–60 cm",
            weight = "3,2–6 kg",
        ),
    )

    val locations: List<AnimalLocationDto> = listOf(
        AnimalLocationDto(
            id = 1,
            name = "Onça-pintada",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -22.9068, longitude = -43.1729),
            locationDescription = "Mata Atlântica — Rio de Janeiro, RJ",
        ),
        AnimalLocationDto(
            id = 2,
            name = "Mico-leão-dourado",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -22.9871, longitude = -43.2486),
            locationDescription = "Reserva Biológica Poço das Antas — Silva Jardim, RJ",
        ),
        AnimalLocationDto(
            id = 3,
            name = "Tucano-toco",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -15.7801, longitude = -47.9292),
            locationDescription = "Cerrado — Brasília, DF",
        ),
        AnimalLocationDto(
            id = 4,
            name = "Arara-azul-grande",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -16.6869, longitude = -49.2648),
            locationDescription = "Pantanal — Goiás",
        ),
        AnimalLocationDto(
            id = 5,
            name = "Capivara",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -23.5505, longitude = -46.6333),
            locationDescription = "Parque Ecológico — São Paulo, SP",
        ),
        AnimalLocationDto(
            id = 6,
            name = "Tamanduá-bandeira",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -19.9167, longitude = -43.9345),
            locationDescription = "Serra do Cipó — Minas Gerais, MG",
        ),
        AnimalLocationDto(
            id = 7,
            name = "Quati",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -22.7489, longitude = -41.8811),
            locationDescription = "Parque Nacional da Serra dos Órgãos — RJ",
        ),
        AnimalLocationDto(
            id = 8,
            name = "Preguiça-de-três-dedos",
            imageUrl = DEFAULT_ANIMAL_IMAGE_URL,
            location = GeoLocationDto(latitude = -3.1190, longitude = -60.0217),
            locationDescription = "Floresta Amazônica — Manaus, AM",
        ),
    )

    private val quizQuestionsByAnimal: Map<Int, List<QuizQuestionDto>> = mapOf(
        1 to listOf(
            QuizQuestionDto(
                statement = "Qual é o habitat principal da onça-pintada na Mata Atlântica?",
                options = """[{"id":1,"value":"Florestas densas e áreas úmidas"},{"id":2,"value":"Desertos áridos"},{"id":3,"value":"Oceanos abertos"}]""",
                code = 101,
            ),
            QuizQuestionDto(
                statement = "A onça-pintada é o maior felino das Américas?",
                options = """[{"id":1,"value":"Sim"},{"id":2,"value":"Não"}]""",
                code = 102,
            ),
        ),
        2 to listOf(
            QuizQuestionDto(
                statement = "O mico-leão-dourado é endêmico de qual região?",
                options = """[{"id":1,"value":"Mata Atlântica do Rio de Janeiro"},{"id":2,"value":"Amazônia"},{"id":3,"value":"Pampa gaúcho"}]""",
                code = 201,
            ),
            QuizQuestionDto(
                statement = "Qual cor caracteriza o mico-leão-dourado?",
                options = """[{"id":1,"value":"Pelagem dourada-laranja"},{"id":2,"value":"Pelagem totalmente preta"},{"id":3,"value":"Pelagem branca"}]""",
                code = 202,
            ),
        ),
        3 to listOf(
            QuizQuestionDto(
                statement = "O tucano-toco se alimenta principalmente de:",
                options = """[{"id":1,"value":"Frutas e insetos"},{"id":2,"value":"Peixes"},{"id":3,"value":"Gramíneas secas"}]""",
                code = 301,
            ),
        ),
    )

    /** Maps question code to the correct option id (first option is always correct in mock data). */
    val correctAnswers: Map<Int, Int> = buildMap {
        quizQuestionsByAnimal.values.flatten().forEach { question ->
            put(question.code, 1)
        }
    }

    fun animalsPage(offset: Int): List<AnimalDto> {
        val start = offset * PAGE_SIZE
        if (start >= animals.size) return emptyList()
        return animals.drop(start).take(PAGE_SIZE)
    }

    fun locationsPage(offset: Int): List<AnimalLocationDto> {
        val start = offset * PAGE_SIZE
        if (start >= locations.size) return emptyList()
        return locations.drop(start).take(PAGE_SIZE)
    }

    fun animalById(id: Int): AnimalDto? = animals.find { it.id == id }

    fun quizQuestions(animalId: Int): List<QuizQuestionDto> =
        quizQuestionsByAnimal[animalId].orEmpty()
}
