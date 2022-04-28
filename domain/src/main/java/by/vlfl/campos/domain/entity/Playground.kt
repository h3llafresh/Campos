package by.vlfl.campos.domain.entity

data class Playground(
    val id: String,
    val name: String,
    val category: SportCategory,
    val address: String,
    val activePlayers: List<User>,
    val coordinates: Coordinates
)