package by.vlfl.campos.presentation.view.main.playground

import android.os.Parcelable
import by.vlfl.campos.domain.entity.Playground
import by.vlfl.campos.domain.entity.SportCategory
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaygroundUiModel(
    val id: String? = "",
    val name: String? = "",
    val address: String? = "",
    val category: SportCategory?,
    val coordinates: LatLng? = null
) : Parcelable

fun Playground.toUiModel(): PlaygroundUiModel = with(this) {
    PlaygroundUiModel(
        id = id,
        name = name,
        address = address,
        category = category,
        coordinates = LatLng(coordinates.latitude!!, coordinates.longitude!!)
    )
}