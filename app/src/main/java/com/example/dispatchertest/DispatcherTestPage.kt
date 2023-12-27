package com.example.dispatchertest

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "!!!"

@Composable
fun DispatcherTest() {

    val scope = rememberCoroutineScope()

    // 負載Dispatcher，調整負載可以負線問題
    DisposableEffect(Unit) {
        val job =
            // 目前只有Dispatchers.Default有問題
            scope.launch(Dispatchers.Default) {
//            scope.launch(Dispatchers.IO) {
//            scope.launch(Dispatchers.Main) {
//            scope.launch(Dispatchers.Unconfined) {
                while (true) {
                    // 如果使用delay(1)降低負載，即可避免Dispatchers.Default無法執行的問題
//                    delay(1)
                }
            }

        onDispose {
            job.cancel()
        }
    }

    // 監控Dispatcher
    DisposableEffect(Unit) {
        Log.d(TAG, "LaunchedEffect")
        val jobDefault = scope.launch(Dispatchers.Default) {
            try {
                while (true) {
                    Log.d(TAG, "Dispatchers.Default")
                    delay(1500)
                }
            } catch (e: Exception) {
                Log.d(TAG, "Dispatchers.Default Exception : $e")
            }

        }
        val jobIO = scope.launch(Dispatchers.IO) {
            try {
                while (true) {
                    Log.d(TAG, "Dispatchers.IO")
                    delay(1500)
                }
            } catch (e: Exception) {
                Log.d(TAG, "Dispatchers.IO Exception : $e")
            }
        }
        val jobMain = scope.launch(Dispatchers.Main) {
            try {
                while (true) {
                    Log.d(TAG, "Dispatchers.Main")
                    delay(1500)
                }
            } catch (e: Exception) {
                Log.d(TAG, "Dispatchers.Main Exception : $e")
            }
        }
        val jobUnconfined = scope.launch(Dispatchers.Unconfined) {
            try {
                while (true) {
                    Log.d(TAG, "Dispatchers.Unconfined")
                    delay(1500)
                }
            } catch (e: Exception) {
                Log.d(TAG, "Dispatchers.Unconfined Exception : $e")
            }
        }

        onDispose {
            jobDefault.cancel()
            jobIO.cancel()
            jobMain.cancel()
            jobUnconfined.cancel()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Test Page\nRead the Log", textAlign = TextAlign.Center) }
}