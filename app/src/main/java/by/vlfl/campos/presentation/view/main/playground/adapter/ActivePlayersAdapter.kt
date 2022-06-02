package by.vlfl.campos.presentation.view.main.playground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.vlfl.campos.databinding.ActivePlayerItemBinding
import by.vlfl.campos.domain.entity.User

class ActivePlayersAdapter(
    private val activePlayers: MutableList<User> = mutableListOf()
) : RecyclerView.Adapter<ActivePlayerViewHolder>() {

    override fun getItemCount() = activePlayers.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivePlayerViewHolder {
        val binding = ActivePlayerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivePlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivePlayerViewHolder, position: Int) = holder.bind(activePlayers[position])

    fun replace(newActivePlayers: List<User>) {
        val diffUtilCallback = ActivePlayersDiffUtilCallback(activePlayers, newActivePlayers)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        activePlayers.clear()
        activePlayers.addAll(newActivePlayers)

        diffResult.dispatchUpdatesTo(this)
    }
}