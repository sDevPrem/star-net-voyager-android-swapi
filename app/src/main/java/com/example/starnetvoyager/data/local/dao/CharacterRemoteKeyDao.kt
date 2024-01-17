package com.example.starnetvoyager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starnetvoyager.data.local.entity.CharacterRemoteKey

@Dao
interface CharacterRemoteKeyDao {
    @Query("SELECT * FROM character_remote_key WHERE id =:id")
    suspend fun getRemoteKeys(id: Int): CharacterRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<CharacterRemoteKey>): List<Long>

    @Query("DELETE FROM character_remote_key")
    suspend fun deleteAllRemoteKeys()

}