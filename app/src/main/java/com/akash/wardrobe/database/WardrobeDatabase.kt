package com.akash.wardrobe.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Top::class,Bottom::class,Favorite::class),version = 1)
public abstract class WardrobeDatabase: RoomDatabase() {
    abstract fun wardrobeDao(): WardrobeDao
}