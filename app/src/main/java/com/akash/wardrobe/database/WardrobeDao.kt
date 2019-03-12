package com.akash.wardrobe.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WardrobeDao {

    @Insert
    fun addTop(top : Top)

    @Insert
    fun addBottom(bottom: Bottom)

    @Query("Select * from top")
    fun getAllTops() : LiveData<List<Top>>

    @Query("Select * from bottom")
    fun getAllBottoms() : LiveData<List<Bottom>>

    @Query("Select * from favorite where topId =:topId AND bottomId =:bottomId")
    fun getFavoriteByIds(topId: Int,bottomId: Int) : Favorite

    @Insert
    fun addToFavorites(favorite: Favorite)

    @Query("Select * from favorite")
    fun getAllFavorites(): List<Favorite>

    @Query("Delete from favorite where topId =:topId AND bottomId =:bottomId")
    fun removeFromFavorite(topId: Int, bottomId: Int)

}