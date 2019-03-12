package com.akash.wardrobe.repository

import androidx.lifecycle.LiveData
import com.akash.wardrobe.database.Bottom
import com.akash.wardrobe.database.Favorite
import com.akash.wardrobe.database.Top
import com.akash.wardrobe.database.WardrobeDao

class WardrobeRepositoryImpl(private val wardrobeDao: WardrobeDao) : WardrobeRepository {

    override fun removeFromFavorites(topId: Int, bottomId: Int) {
        wardrobeDao.removeFromFavorite(topId,bottomId)
    }

    override fun getAllFavorites(): List<Favorite> = wardrobeDao.getAllFavorites()

    override fun getFavoriteByIds(topId: Int, bottomId: Int): Favorite = wardrobeDao.getFavoriteByIds(topId,bottomId)

    override fun addToFavorites(topId: Int,bottomId: Int) {
        val favorite = Favorite(0,topId,bottomId)
        wardrobeDao.addToFavorites(favorite)
    }

    override fun getAllTops(): LiveData<List<Top>> = wardrobeDao.getAllTops()

    override fun getAllBottoms(): LiveData<List<Bottom>> = wardrobeDao.getAllBottoms()

    override fun addTop(top: Top) {
        wardrobeDao.addTop(top)
    }

    override fun addBottom(bottom: Bottom) {
        wardrobeDao.addBottom(bottom)
    }
}