package aaronstacy.esvbible.data

import android.os.Looper
import android.util.Log.d
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

private const val TAG_BASE = "EsvBible"

fun log(tag: String, message: String) {
  val mainLooperThreadId = Looper.getMainLooper().thread.id
  val currentThreadId = Thread.currentThread().id
  val tidString = if (mainLooperThreadId == currentThreadId) "main" else "$currentThreadId"
  d("$TAG_BASE$tag", "[TID: $tidString] $message")
}

private fun l(tag: String, message: String) = log(tag, message)

fun <T> Flow<T>.log(tag: String, message: String): Flow<T> = onEach { l(tag, "$message $it") }