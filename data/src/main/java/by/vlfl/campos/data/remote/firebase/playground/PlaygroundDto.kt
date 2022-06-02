package by.vlfl.campos.data.remote.firebase.playground

import by.vlfl.campos.domain.entity.Coordinates
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.SportCategory
import com.google.firebase.database.IgnoreExtraProperties

private const val SPORT_CATEGORY_BASKETBALL = "basketball"
private const val SPORT_CATEGORY_FOOTBALL = "football"
private const val SPORT_CATEGORY_VOLLEYBALL = "volleyball"

@IgnoreExtraProperties
data class PlaygroundDto(
    val id: String? = "",
    val name: String? = "",
    val category: String? = "",
    val address: String? = "",
    val playersNumber: Int = 0,
    val coordinates: Coordinates? = null
)

fun PlaygroundDto.toDomainModel() = Playground(
    id = checkNotNull(this.id),
    name = checkNotNull(this.name),
    category = mapDtoCategoryToDomainModel(this.category),
    address = checkNotNull(this.address),
    activePlayersNumber = this.playersNumber,
    coordinates = checkNotNull(this.coordinates)
)

fun mapDtoCategoryToDomainModel(category: String?) = when (category) {
    SPORT_CATEGORY_BASKETBALL -> SportCategory.Basketball
    SPORT_CATEGORY_FOOTBALL -> SportCategory.Football
    SPORT_CATEGORY_VOLLEYBALL -> SportCategory.Volleyball
    else -> throw IllegalStateException("Unknown type of sport category")
}