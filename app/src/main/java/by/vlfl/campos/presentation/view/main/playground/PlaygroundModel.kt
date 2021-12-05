package by.vlfl.campos.presentation.view.main.playground

import android.os.Parcelable
import by.vlfl.campos.domain.entity.Playground
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaygroundModel(
    val id: String? = "",
    val name: String? = "",
    val address: String? = "",
    val category: String? = "",
    val coordinates: LatLng? = null
) : Parcelable {
    companion object {
        fun fromDomainModel(playground: Playground): PlaygroundModel {
            return with(playground) {
                PlaygroundModel(
                    id = id,
                    name = name,
                    address = address,
                    category = category,
                    coordinates = LatLng(coordinates!!.latitude!!, coordinates!!.longitude!!),
                )
            }
        }
    }
}