package com.example.starnetvoyager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.starnetvoyager.data.network.model.CharacterResponse

@Entity(tableName = "character")
data class Character(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(TableInfo.ID_COLUMN_NAME)
    val id: Int,
    @ColumnInfo(name = TableInfo.NAME_COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = TableInfo.GENDER_COLUMN_NAME)
    val gender: String,
    @ColumnInfo(name = TableInfo.HEIGHT_COLUMN_NAME)
    val height: String,
    @ColumnInfo(name = TableInfo.BIRTH_YEAR_COLUMN_NAME)
    val birthYear: String,
) {

    object TableInfo {
        const val ID_COLUMN_NAME = "id"
        const val NAME_COLUMN_NAME = "name"
        const val GENDER_COLUMN_NAME = "gender"
        const val HEIGHT_COLUMN_NAME = "height"
        const val BIRTH_YEAR_COLUMN_NAME = "birth_year"
    }

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
