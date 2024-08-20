package com.example.timerangesh

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.random.Random

//---------------------Homeスクリーン---------------------
@Composable
fun HomeScreen() {
    //---------------------各入力フィールドのラベル---------------------
    val timeText = arrayOf("開始時刻", "終了時刻", "検索時刻")

    //---------------------入力値を保持するための状態---------------------
    val inputValues = rememberSaveable { mutableStateOf(Triple("", "", "")) }
    //---------------------検索結果を表示するかどうかを制御するための状態---------------------
    val showResult = rememberSaveable { mutableStateOf(false) }
    //---------------------結果が保存されたかどうかを示すフラグ---------------------
    val resultSaved = rememberSaveable { mutableStateOf(false) }
    //---------------------各フィールドのエラーメッセージを保持するための状態---------------------
    val errorMessages = rememberSaveable { mutableStateOf(mapOf<Int, String>()) }

    val context = LocalContext.current

    //---------------------入力フィールドをクリアする関数---------------------
    fun clearInputs() {
        inputValues.value = Triple("", "", "")
        showResult.value = false // 検索結果を非表示
        resultSaved.value = false // クリア時に保存フラグもリセット
        errorMessages.value = mapOf()
    }

    //---------------------入力値の検証を行う関数---------------------
    fun validateInput(index: Int, value: String) {
        val errorMessage = when {
            value.isEmpty() -> null
            value.toIntOrNull() !in 1..24 -> when (index) {
                0 -> "開始時刻は1〜24の範囲で入力してください。"
                1 -> "終了時刻は1〜24の範囲で入力してください。"
                2 -> "検索時刻は1〜24の範囲で入力してください。"
                else -> null
            }
            else -> null
        }
        //---------------------エラーメッセージを更新---------------------
        errorMessages.value = errorMessages.value.toMutableMap().apply {
            if (errorMessage != null) {
                this[index] = errorMessage
            } else {
                remove(index)
            }
        }
    }

    //---------------------終了時刻が開始時刻より早くないか検証する関数---------------------
    fun validateEndTime() {
        val startTime = inputValues.value.first.toIntOrNull() ?: 0
        val endTime = inputValues.value.second.toIntOrNull() ?: 0

        if (endTime in 1..<startTime) {
            errorMessages.value = errorMessages.value.toMutableMap().apply {
                this[1] = "開始時刻以上の値を入力してください。"
            }
        } else null
    }

    //---------------------入力が有効かどうかを判断する関数---------------------
    fun areInputsValid(): Boolean {
        validateEndTime()
        return errorMessages.value.isEmpty() &&
                listOf(inputValues.value.first, inputValues.value.second, inputValues.value.third)
                    .all { it.isNotEmpty() && it.toIntOrNull() in 1..24 }
    }

    //---------------------ランダムな時刻を設定する関数---------------------
    fun setRandomTimes() {
        generateRandomTimes(
            onStartTimeGenerated = { startTime ->
                inputValues.value = inputValues.value.copy(first = startTime)
                validateInput(0, startTime)
                validateEndTime()
            },
            onEndTimeGenerated = { endTime ->
                inputValues.value = inputValues.value.copy(second = endTime)
                validateInput(1, endTime)
                validateEndTime()
            },
            onSearchTimeGenerated = { searchTime ->
                inputValues.value = inputValues.value.copy(third = searchTime)
                validateInput(2, searchTime)
            }
        )
        showResult.value = false  // 検索結果を非表示
        resultSaved.value = false // ランダム時刻を設定した際に保存フラグもリセット
    }

    //---------------------検索結果を保存する処理---------------------
    LaunchedEffect(showResult.value) {
        //---------------------検索結果が表示されており、データ保存しておらず、入力値が有効な時に保存可能---------------------
        if (showResult.value && !resultSaved.value && areInputsValid()) {
            val startTime = inputValues.value.first
            val endTime = inputValues.value.second
            val searchTime = inputValues.value.third
            val contains = searchTime.toIntOrNull() in startTime.toIntOrNull()!!..endTime.toIntOrNull()!!

            saveSearchResult(context, startTime, endTime, searchTime, contains)
            resultSaved.value = true // データを保存した後にフラグを更新
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Column(
                    verticalArrangement = Arrangement.Top
                ) {
                    Text("それぞれの時刻を入力して検索、")
                    Text("又はランダムに時刻を指定して検索してください。")
                    Text("検索結果は下に表示されます。")
                    Text("過去の検索結果はDataタブをタップしてください。")
                }

                Row {
                    //---------------------開始時刻入力フィールド---------------------
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.3f),
                        contentAlignment = Alignment.Center
                    ) {
                        InputTextField(
                            timeText[0],
                            value = inputValues.value.first,
                            onValueChange = { newValue ->
                                inputValues.value = inputValues.value.copy(first = newValue)
                                validateInput(0, newValue)
                                validateEndTime()
                                showResult.value = false // 入力変更時に検索結果を非表示
                                resultSaved.value = false // 保存フラグをリセット
                            },
                            errorMessage = errorMessages.value[0]
                        )
                    }
                    //---------------------終了時刻入力フィールド---------------------
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.3f),
                        contentAlignment = Alignment.Center
                    ) {
                        InputTextField(
                            timeText[1],
                            value = inputValues.value.second,
                            onValueChange = { newValue ->
                                inputValues.value = inputValues.value.copy(second = newValue)
                                validateInput(1, newValue)
                                validateEndTime()
                                showResult.value = false // 入力変更時に検索結果を非表示
                                resultSaved.value = false // 保存フラグをリセット
                            },
                            errorMessage = errorMessages.value[1]
                        )
                    }
                    //---------------------検索時刻入力フィールド---------------------
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(0.3f),
                        contentAlignment = Alignment.Center
                    ) {
                        InputTextField(
                            timeText[2],
                            value = inputValues.value.third,
                            onValueChange = { newValue ->
                                inputValues.value = inputValues.value.copy(third = newValue)
                                validateInput(2, newValue)
                                showResult.value = false // 入力変更時に検索結果を非表示
                                resultSaved.value = false // 保存フラグをリセット
                            },
                            errorMessage = errorMessages.value[2]
                        )
                    }
                }
                Row {
                    //---------------------ランダムに時刻を指定するボタン---------------------
                    Box(
                        modifier = Modifier
                            .weight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { setRandomTimes() }) {
                            Text("ランダムに時刻を指定", fontSize = 12.sp)
                        }
                    }
                    //---------------------入力をクリアするボタン---------------------
                    Box(
                        modifier = Modifier
                            .weight(0.25f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { clearInputs() }) {
                            Text("クリア")
                        }
                    }
                    //---------------------検索を実行するボタン---------------------
                    Box(
                        modifier = Modifier
                            .weight(0.3f),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { showResult.value = true },
                            enabled = areInputsValid()
                        ) {
                            Text("検索")
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                //---------------------検索結果を表示---------------------
                if (showResult.value) {
                    DisplayResult(
                        value1 = inputValues.value.first,
                        value2 = inputValues.value.second,
                        value3 = inputValues.value.third
                    )
                }
            }
        }
    }
}

//---------------------時刻入力用のテキストボックス---------------------
@Composable
fun InputTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String? = null
) {
    val maxChar = 2 // 最大入力文字数

    Column {
        TextField(
            value = value,
            onValueChange = {
                if (it.length <= maxChar) {
                    onValueChange(it)
                }
            },
            label = { Text(label) },
            modifier = Modifier.padding(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )
        //---------------------エラーメッセージを表示---------------------
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            )
        }
    }
}

//---------------------検索結果を表示---------------------
@Composable
fun DisplayResult(value1: String, value2: String, value3: String) {
    //---------------------入力された値を整数に変換---------------------
    val startTime = value1.toIntOrNull() ?: 0
    val endTime = value2.toIntOrNull() ?: 0
    val searchTime = value3.toIntOrNull() ?: 0

    //---------------------判定処理---------------------
    val resultMessage = when (searchTime) {
        in startTime..< endTime -> "検索時刻 ($searchTime) は範囲内に含まれます。"
        startTime -> "検索時刻 ($searchTime) は範囲内に含まれます。"
        else -> "検索時刻 ($searchTime) は範囲内に含まれません。"
    }

    //---------------------判定結果を表示---------------------
    Text(
        text = resultMessage,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyMedium,
        fontSize = 20.sp
    )
}

//---------------------ランダムな時刻を生成し、開始時刻、終了時刻、検索時刻を設定する関数---------------------
fun generateRandomTimes(
    onStartTimeGenerated: (String) -> Unit,
    onEndTimeGenerated: (String) -> Unit,
    onSearchTimeGenerated: (String) -> Unit
) {
    val random = Random.Default
    //---------------------開始時刻を1〜24の範囲でランダムに生成---------------------
    val startTime = random.nextInt(1, 25)
    //---------------------終了時刻は開始時刻以上でランダムに生成---------------------
    val endTime = random.nextInt(startTime, 25)
    //---------------------検索時刻を1〜24の範囲でランダムに生成---------------------
    val searchTime = random.nextInt(1, 25)

    //---------------------生成した時刻を文字列に変換してコールバックで渡す---------------------
    onStartTimeGenerated(startTime.toString())
    onEndTimeGenerated(endTime.toString())
    onSearchTimeGenerated(searchTime.toString())
}

//---------------------検索結果をデータベースに保存する関数---------------------
suspend fun saveSearchResult(
    context: Context,
    startTime: String,
    endTime: String,
    searchTime: String,
    contains: Boolean
) {
    val database = ResultData.getDatabase(context)
    val searchResultDao = database.searchResultDao()
    val currentDateTime = getCurrentDateTime()

    val result = SearchResult(
        dateTime = currentDateTime, // 現在の日時
        startTime = startTime, // 開始時刻
        endTime = endTime, // 終了時刻
        searchTime = searchTime, // 検索時刻
        contains = contains // 検索時刻が含まれるか
    )
    searchResultDao.insert(result)
}

//---------------------現在時刻を取得する関数---------------------
fun getCurrentDateTime(): String {
    //---------------------日本時間（JST）に設定されたSimpleDateFormatインスタンスを作成---------------------
    val dateFormat = SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss", Locale.JAPAN).apply {
        timeZone = TimeZone.getTimeZone("Asia/Tokyo")
    }
    return dateFormat.format(Date())
}
