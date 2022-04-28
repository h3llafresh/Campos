package by.vlfl.campos.presentation.view.main.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterResultModel(
    val activePlayersFilters: List<ActivePlayersRange>,
    val sportCategoryFilters: List<String>
) : Parcelable