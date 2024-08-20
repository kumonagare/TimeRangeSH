package com.example.timerangesh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.timerangesh.ui.theme.TimeRangeSHTheme

//---------------------MainActivity はアプリケーションのエントリーポイントで、UI の初期設定を行う---------------------
class MainActivity : ComponentActivity() {

    //---------------------アクティビティの作成時に呼ばれるメソッド---------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //---------------------エッジトゥエッジ表示を有効にするメソッド（スクリーン全体を使うための設定）---------------------
        enableEdgeToEdge()

        //---------------------コンテンツを設定する---------------------
        setContent {
            //---------------------アプリケーションのテーマを設定---------------------
            TimeRangeSHTheme {
                //---------------------メインレイアウトを表示---------------------
                MainLayout()
            }
        }
    }
}