package com.example.starnetvoyager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_remote_key")
data class CharacterRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @ColumnInfo(name = "prev_page")
    val prevPage: Int? = null,
    @ColumnInfo(name = "next_page")
    val nextPage: Int? = null
)