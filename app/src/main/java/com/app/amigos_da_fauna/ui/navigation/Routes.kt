package com.app.amigos_da_fauna.ui.navigation

object Routes {
    const val HOME = "home"
    const val MAP = "map"
    const val PROFILE = "profile"
    const val ABOUT = "about"
    const val ANIMAL_DETAIL = "animals/{animalId}"
    const val QUIZ = "quiz/{animalId}"
    const val REGISTER = "register"

    fun animalDetail(animalId: Int) = "animals/$animalId"
    fun quiz(animalId: Int) = "quiz/$animalId"
}

val bottomNavRoutes = setOf(Routes.HOME, Routes.MAP, Routes.PROFILE, Routes.ABOUT)
