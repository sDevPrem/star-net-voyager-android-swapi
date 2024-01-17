package com.example.starnetvoyager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.starnetvoyager.data.network.model.CharacterResponse

@Entity(tableName = "character")
data class Character(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "height")
    val height: String,
    @ColumnInfo(name = "birth_year")
    val birthYear: String,
) {
    companion object {
        fun CharacterResponse.toCharacter() = Character(
            name = name,
            gender = gender,
            height = height,
            birthYear = birthYear,
            id = id
        )
    }
}
