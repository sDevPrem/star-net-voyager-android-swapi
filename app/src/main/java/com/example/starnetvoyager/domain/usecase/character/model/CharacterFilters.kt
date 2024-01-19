package com.example.starnetvoyager.domain.usecase.character.model

data class CharacterFilters(
    val searchQuery: String = "",
    val selectedGender: Gender = Gender.ALL
) {
    enum class Gender {
        MALE,
        FEMALE,
        OTHER,
        ALL
    }
}