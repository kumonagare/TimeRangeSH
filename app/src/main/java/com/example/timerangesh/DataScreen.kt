package com.example.timerangesh

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DataScreen() {
    val context = LocalContext.current
    val database = ResultData.getDatabase(context)
    val searchResultDao = database.searchResultDao()

    var searchResults by remember { mutableStateOf<List<SearchResult>>(listOf()) }

    //---------------------データを取得する---------------------
    LaunchedEffect(Unit) {
        searchResults = searchResultDao.getAll()
    }

    //---------------------列数の定義---------------------
    val columns = listOf("日時", "開始時刻", "終了時刻", "検索時刻", "含むか")

    //---------------------スクロール可能なレイアウト---------------------
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        //---------------------ヘッダー行---------------------
        item {
            Row(
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .background(Color.LightGray)
                    .fillMaxWidth()
            ) {
                columns.forEach { header ->
                    Text(
                        text = header,
                        modifier = Modifier
                            .weight(1f)
                            .border(0.5.dp, Color.Black)
                            .background(Color.LightGray),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        //---------------------データ行---------------------
        items(searchResults) { result ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = result.dateTime,
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.Black),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
                Text(
                    text = result.startTime,
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.Black)
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Text(
                    text = result.endTime,
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.Black)
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Text(
                    text = result.searchTime,
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.Black)
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Text(
                    text = if (result.contains) "含む" else "含まない",
                    modifier = Modifier
                        .weight(1f)
                        .border(0.5.dp, Color.Black)
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
        }
    }
}
