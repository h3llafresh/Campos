package by.vlfl.campos.presentation.view.main.playground.adapter

import androidx.recyclerview.widget.RecyclerView
import by.vlfl.campos.databinding.ActivePlayerItemBinding
import by.vlfl.campos.domain.entity.User

class ActivePlayerViewHolder(
    private val binding: ActivePlayerItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(activePlayer: User) {
        binding.tvPlayerName.text = activePlayer.name
    }
}