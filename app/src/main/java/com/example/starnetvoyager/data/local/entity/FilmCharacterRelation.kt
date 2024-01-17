package com.example.starnetvoyager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "film_character",
    primaryKeys = [
        FilmCharacterRelation.TableInfo.FILM_ID_COLUMN_NAME,
        FilmCharacterRelation.TableInfo.CHARACTER_ID_COLUMN_NAME
    ],
)
data class FilmCharacterRelation(
    @ColumnInfo(TableInfo.FILM_ID_COLUMN_NAME)
    val filmId: Int,
    @ColumnInfo(TableInfo.CHARACTER_ID_COLUMN_NAME)
    val characterId: Int,
) {
    object TableInfo {
        const val FILM_ID_COLUMN_NAME = "film_id"
        const val CHARACTER_ID_COLUMN_NAME = "character_id"
    }
}