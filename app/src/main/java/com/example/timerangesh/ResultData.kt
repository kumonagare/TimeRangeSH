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

//---------------------データベースエンティティ: 検索結果を表すデータクラス---------------------
@Entity(tableName = "search_results")
data class SearchResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 一意のID（自動生成）
    val dateTime: String,  // データの日時
    val startTime: String, // 開始時刻
    val endTime: String,   // 終了時刻
    val searchTime: String, // 検索時刻
    val contains: Boolean // 検索時刻が範囲内かどうか
)

//---------------------データアクセスオブジェクト (DAO): データベース操作のためのインターフェース---------------------
@Dao
interface SearchResultDao {
    //---------------------検索結果をデータベースに挿入するメソッド---------------------
    @Insert
    suspend fun insert(result: SearchResult)

    //---------------------データベースからすべての検索結果を取得するメソッド---------------------
    @Query("SELECT * FROM search_results")
    suspend fun getAll(): List<SearchResult>
}

//---------------------Roomデータベース: データベースの構成を定義する抽象クラス---------------------
@Database(entities = [SearchResult::class], version = 1)
abstract class ResultData : RoomDatabase() {
    //---------------------DAOを取得するための抽象メソッド---------------------
    abstract fun searchResultDao(): SearchResultDao

    companion object {
        @Volatile
        private var INSTANCE: ResultData? = null // データベースインスタンスのキャッシュ

        //---------------------データベースインスタンスを取得するためのメソッド---------------------
        fun getDatabase(context: Context): ResultData {
            //---------------------インスタンスがまだ存在しない場合は、同期して新たに作成---------------------
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResultData::class.java, // データベースクラス
                    "app_database" // データベース名
                ).build()
                INSTANCE = instance // インスタンスをキャッシュ
                instance
            }
        }
    }
}
