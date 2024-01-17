package com.example.starnetvoyager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.starnetvoyager.data.local.entity.FilmCharacterRelation

@Dao
interface FilmCharacterDao {

    @Insert
    fun insertFilmCharacterRelations(relations: List<FilmCharacterRelation>): List<Long>

    @Query(
        "DELETE FROM film_character WHERE character_id = :characterId"
    )
    fun deleteFilmRelationOfCharacter(characterId: Int)
}