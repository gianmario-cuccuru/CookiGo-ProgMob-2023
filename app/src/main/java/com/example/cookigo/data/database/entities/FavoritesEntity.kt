package com.example.cookigo.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cookigo.util.Constants.Companion.FAVORITE_RECIPES_TABLE
import com.example.cookigo.models.Result

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)