package com.akash.wardrobe.repository

import androidx.lifecycle.LiveData
import com.akash.wardrobe.database.Bottom
import com.akash.wardrobe.database.Favorite
import com.akash.wardrobe.database.Top

interface WardrobeRepository {
    fun addTop(top: Top)
    fun addBottom(bottom: Bottom)
    fun getAllTops(): LiveData<List<Top>>
    fun getAllBottoms(): LiveData<List<Bottom>>
    fun addToFavorites(topId: Int,bottomId: Int)
    fun getFavoriteByIds(topId: Int,bottomId: Int) : Favorite
    fun getAllFavorites() : List<Favorite>
    fun removeFromFavorites(topId: Int,bottomId: Int)
}