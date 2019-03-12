package com.akash.wardrobe.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bottom")
data class Bottom(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var image : String
)