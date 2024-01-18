package com.example.starnetvoyager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.starnetvoyager.data.network.model.FilmResponse
import com.example.starnetvoyager.domain.entity.StarWarsMovie

@Entity
data class Film(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(TableInfo.ID_COLUMN_NAME)
    val id: Int,
    @ColumnInfo(TableInfo.OPENING_CRAWL_COLUMN_NAME)
    val openingCrawl: String,
    @ColumnInfo(TableInfo.TITLE_COLUMN_NAME)
    val title: String
) {
    object TableInfo {
        const val ID_COLUMN_NAME = "id"
        const val OPENING_CRAWL_COLUMN_NAME = "opening_crawl"
        const val TITLE_COLUMN_NAME = "title"
    }

    companion object {
        fun FilmResponse.toFilm() = Film(
            id = id,
            openingCrawl = openingCrawl,
            title = title
        )
    }
}

fun Film.toDomain() = StarWarsMovie(
    id = id,
    openingCrawl = openingCrawl,
    title = title
)
