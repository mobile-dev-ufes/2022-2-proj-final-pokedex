package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="Types", indices = [Index(value = ["id"], unique = true)])
class TypeEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Int = 0

    @ColumnInfo(name="name")
    var name: String = ""
}