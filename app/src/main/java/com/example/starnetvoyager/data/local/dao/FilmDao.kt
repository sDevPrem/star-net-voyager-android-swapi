package com.example.starnetvoyager.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starnetvoyager.data.local.entity.Film

@Dao
interface FilmDao {

    @Query(
        "SELECT * FROM film " +
                "INNER JOIN film_character fc on film_id == film.id " +
                "WHERE fc.character_id = :characterId"
    )
    fun getFilmsOfCharacter(characterId: Int): PagingSource<Int, Film>

    @Query(
        "DELETE FROM film " +
                "WHERE id in (SELECT film_id FROM film_character fc WHERE fc.character_id = :characterId)"
    )
    fun deleteFilmsOfCharacter(characterId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilms(films: List<Film>): List<Long>
}