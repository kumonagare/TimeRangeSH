package com.example.timerangesh

import android.content.Context

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "search_results")
data class SearchResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: String,  // データの日時
    val startTime: String,
    val endTime: String,
    val searchTime: String,
    val contains: Boolean // 検索時刻が範囲内かどうか
)

// DAO
@Dao
interface SearchResultDao {
    @Insert
    suspend fun insert(result: SearchResult)

    @Query("SELECT * FROM search_results")
    suspend fun getAll(): List<SearchResult>
}

@Database(entities = [SearchResult::class], version = 1)
abstract class ResultData : RoomDatabase() {
    abstract fun searchResultDao(): SearchResultDao

    companion object {
        @Volatile
        private var INSTANCE: ResultData? = null

        fun getDatabase(context: Context): ResultData {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResultData::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


