package by.vlfl.campos.presentation.view.main.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ActivePlayersRange(
    val start: Int,
    val end: Int
) : Parcelable {
    companion object {
        public val EMPTY = ActivePlayersRange(0, 0)
    }
}
