package com.akash.wardrobe.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akash.wardrobe.database.Bottom
import com.akash.wardrobe.database.Top
import com.akash.wardrobe.repository.WardrobeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainViewModel(private val wardrobeRepository: WardrobeRepository) : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName
    private var favoriteList: ArrayList<Pair<Int, Int>> = ArrayList()

    /**
    * viewModelScopes are added in latest version of lifecycle component library.
    */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            wardrobeRepository.getAllFavorites().forEach {
                favoriteList.add(Pair(it.topId, it.bottomId))
            }
        }
    }

    /**
     * This function is adds the top object in Top table.
     */
    fun addTop(top: Top) {
        viewModelScope.launch(Dispatchers.IO) {
            wardrobeRepository.addTop(top)
        }
    }

    /**
     * This function is adds bottom object in Bottom table.
     */
    fun addBottom(bottom: Bottom) {
        viewModelScope.launch(Dispatchers.IO) {
            wardrobeRepository.addBottom(bottom)
        }
    }

    /**
     * This functions returns livedata of list of Top objects saved in Top table.
     */
    fun getAllTops(): LiveData<List<Top>> {
        return wardrobeRepository.getAllTops()
    }

    /**
     * This functions returns livedata of list of Bottom objects saved in Bottom table.
     */
    fun getAllBottoms(): LiveData<List<Bottom>> {
        return wardrobeRepository.getAllBottoms()
    }

    /**
     * This function used to convert bitmap to ByteArray so that we can store bitmaps as ByteArray in Database.
     */
    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArray: ByteArray
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        byteArray = stream.toByteArray()
        bitmap.recycle()
        return byteArray
    }

    /**
     * This function adds particular combination of top and bottom ids in favorite table.
     */
    fun addToFavorite(top: Top, bottom: Bottom) {
        val topId = top.id
        val bottomId = bottom.id
        Log.e(TAG, " topId = $topId, bottomId = $bottomId")
        viewModelScope.launch(Dispatchers.IO) {
            wardrobeRepository.addToFavorites(topId, bottomId)
        }
        favoriteList.add(Pair(topId, bottomId))
    }

    /**
     * This function removes particular combination of top and bottom ids from favorite table.
     */
    fun removeFromFavorite(top: Top,bottom: Bottom){
        val topId = top.id
        val bottomId = bottom.id
        Log.e(TAG, " topId = $topId, bottomId = $bottomId")
        viewModelScope.launch(Dispatchers.IO) {
            wardrobeRepository.addToFavorites(topId, bottomId)
        }
        removeFromFavoriteList(topId,bottomId)
    }

    /**
     * This function removes particular Pair from favoriteList maintained in this ViewModel.
     */
    private fun removeFromFavoriteList(topId: Int, bottomId: Int) {
        favoriteList = favoriteList
            .filter { it.first != topId && it.second != bottomId } as ArrayList<Pair<Int, Int>>
    }

    /**
     * This function checks if particular ids of top and bottom are present in favoriteList.
     */
    fun isFavorite(topId: Int, bottomId: Int): Boolean {
        var isFavorite = false
        favoriteList.forEach {
            if (it.first == topId && it.second == bottomId) {
                isFavorite = true
            }
        }
        return isFavorite
    }
}