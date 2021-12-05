package by.vlfl.campos.presentation.view.main.playground.adapter

import androidx.recyclerview.widget.DiffUtil
import by.vlfl.campos.domain.entity.User

class ActivePlayersDiffUtilCallback(
    private val oldPlayersList: List<User>,
    private val newPlayersList: List<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldPlayersList.size

    override fun getNewListSize(): Int = newPlayersList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldPlayersList[oldItemPosition].id == newPlayersList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldPlayersList[oldItemPosition] == newPlayersList[newItemPosition]
}