@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.timerangesh

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.timerangesh.ui.theme.NotoSerifJpSemiBoldFont
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//---------------------メインレイアウトの定義---------------------
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainLayout() {
    //---------------------Navigation Controllerの作成。画面遷移を管理する。---------------------
    val navController = rememberNavController()

    //---------------------Scaffoldコンポーネントを使用して、アプリ全体のレイアウトを構築---------------------
    Scaffold(
        //---------------------トップバーの設定---------------------
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    //---------------------トップバーの背景色とタイトルの色を設定---------------------
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    //---------------------トップバーに表示されるアプリタイトル---------------------
                    Text("時刻検索アプリ", fontFamily = NotoSerifJpSemiBoldFont)
                }
            )
        },
        //---------------------ボトムナビゲーションバーの設定---------------------
        bottomBar = {
            BottomNavigation(navController) // ナビゲーションバーを表示し、画面遷移をサポート
        },
    ) { innerPadding ->
        //---------------------内部コンテンツを配置するカラム---------------------
        Column(
            modifier = Modifier
                .padding(innerPadding), // Scaffoldから提供される内側のパディングを設定
            verticalArrangement = Arrangement.spacedBy(16.dp), // 各コンテンツ間のスペースを設定
        ) {
            //---------------------ナビゲーションホストを使って画面遷移を管理---------------------
            NavHost(
                modifier = Modifier
                    .fillMaxHeight() // 高さを画面いっぱいに設定
                    .fillMaxWidth(), // 幅を画面いっぱいに設定
                navController = navController, // ナビゲーションコントローラーを設定
                startDestination = BottomNavItem.Home.route // 初期表示画面を設定
            ) {
                //---------------------ナビゲーションクリームで各画面のルート設定---------------------
                composable(BottomNavItem.Home.route) { HomeScreen() }
                composable(BottomNavItem.Data.route) { DataScreen() }
            }
        }
    }
}

//---------------------アプリケーション全体の依存性注入を設定---------------------
@HiltAndroidApp
class App : Application() {}

//---------------------HomeScreenのViewModelを設定---------------------
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {}

//---------------------ボトムナビゲーションアイテムの定義---------------------
sealed class BottomNavItem(val route: String, val icon: @Composable () -> Unit, val title: String) {
    data object Home : BottomNavItem("home", { Icon(Icons.Default.Home, contentDescription = "Home") }, "Home")
    data object Data : BottomNavItem("Data", { Icon(painterResource(id = R.drawable.baseline_storage_24), contentDescription = "Data") }, "Data")
}

//---------------------ボトムナビゲーションコンポーザブル---------------------
@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Data,
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { item.icon() },  // アイコンの描画をラムダ式で呼び出す
                label = { Text(item.title) }, // アイコンのラベルを設定
                selected = currentRoute == item.route, // 現在のルートと一致するかチェック
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { // スタートデスティネーションまでポップ
                            saveState = true
                        }
                        launchSingleTop = true // 同じルートの重複を避ける
                        restoreState = true // 状態を復元
                    }
                }
            )
        }
    }
}
