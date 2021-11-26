package by.vlfl.campos.presentation.view.main.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileModel(
    val id: String,
    val name: String,
    val birthDate: String
) : Parcelable