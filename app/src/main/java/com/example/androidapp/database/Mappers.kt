package com.example.androidapp.database

import com.example.androidapp.screens.MarvelHero
import com.example.androidapp.screens.Hero

fun MarvelHero.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.thumbnail.fullPath
    )
}


fun CharacterEntity.toUI(): Hero {
    return Hero(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.image
    )
}

fun MarvelHero.toUI(): Hero {
    return Hero(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.thumbnail.fullPath
    )
}
