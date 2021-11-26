package by.vlfl.campos.domain.repostitory

import by.vlfl.campos.domain.entity.Playground

interface IPlaygroundRepository {
    fun getPlaygrounds(): List<Playground>
}