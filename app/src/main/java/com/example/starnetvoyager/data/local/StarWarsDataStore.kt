package com.example.starnetvoyager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.starnetvoyager.data.local.dao.CharacterDao
import com.example.starnetvoyager.data.local.dao.CharacterRemoteKeyDao
import com.example.starnetvoyager.data.local.entity.Character
import com.example.starnetvoyager.data.local.entity.CharacterRemoteKey

@Database(entities = [Character::class, CharacterRemoteKey::class], version = 1)
abstract class StarWarsDataStore : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun characterRemoteKeyDao(): CharacterRemoteKeyDao

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
