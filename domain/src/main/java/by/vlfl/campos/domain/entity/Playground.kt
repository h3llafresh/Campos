package by.vlfl.campos.domain.entity

data class Playground(
    val id: String? = "",
    val name: String? = "",
    val category: String? = "",
    val address: String? = "",
    val coordinates: Coordinates ?= null,
    val activePlayers: HashMap<String, Boolean> ?= null
)