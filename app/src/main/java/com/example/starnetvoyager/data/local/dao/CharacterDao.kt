package com.example.starnetvoyager.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starnetvoyager.data.local.entity.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getCharacters(): PagingSource<Int, Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character): Long

    @Query("DELETE FROM character")
    suspend fun deleteCharacters()
}