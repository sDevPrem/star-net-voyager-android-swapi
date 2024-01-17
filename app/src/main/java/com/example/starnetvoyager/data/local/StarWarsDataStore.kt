package com.example.starnetvoyager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.starnetvoyager.data.local.dao.CharacterDao
import com.example.starnetvoyager.data.local.dao.CharacterRemoteKeyDao
import com.example.starnetvoyager.data.local.dao.FilmCharacterDao
import com.example.starnetvoyager.data.local.dao.FilmDao
import com.example.starnetvoyager.data.local.entity.Character
import com.example.starnetvoyager.data.local.entity.CharacterRemoteKey
import com.example.starnetvoyager.data.local.entity.Film
import com.example.starnetvoyager.data.local.entity.FilmCharacterRelation

@Database(
    entities = [
        Character::class,
        CharacterRemoteKey::class,
        Film::class,
        FilmCharacterRelation::class
    ],
    version = 1
)
abstract class StarWarsDataStore : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun characterRemoteKeyDao(): CharacterRemoteKeyDao

    abstract fun filmDao(): FilmDao

    abstract fun filmCharacterDao(): FilmCharacterDao

    companion object {
        private const val DATABASE_NAME = "star_net_voyager_database"
        fun createDatabase(context: Context) =
            Room
                .databaseBuilder(
                    context,
                    StarWarsDataStore::class.java,
                    DATABASE_NAME
                )
                .build()
    }
}
