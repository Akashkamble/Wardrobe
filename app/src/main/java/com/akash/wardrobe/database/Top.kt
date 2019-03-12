package com.akash.wardrobe.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top")
data class Top(
  @PrimaryKey(autoGenerate = true)
  var id: Int,
  var image : String
)
