package com.qianyue.wanandroidmvi.ext

import android.app.Dialog
import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.qianyue.wanandroidmvi.utils.WanImageLoader
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * @author QianYue
 * @since 2023/8/15
 */

fun Context.dp2px(dp: Float): Float {
    return resources.displayMetrics.density * dp
}

fun Context.loadImage(url: String?, iv: ImageView) {
    WanImageLoader.loadImage(this, url, iv)
}

@ColorInt
fun Context.getThemeColor(attr: Int, defColor: Int = 0): Int {
    val typedValue = obtainStyledAttributes(intArrayOf(attr))
    val result = typedValue.getColor(0, defColor)
    typedValue.recycle()
    return result
}

suspend fun <T> AppCompatActivity.showDialogSuspend(
    factory: (Channel<T?>) -> Dialog
): T? {
    val channel = Channel<T?>()

    val dialog = factory(channel)
    dialog.setOnDismissListener {
        runCatching {
            lifecycle.coroutineScope.launch {
                channel.send(null)
            }
        }
    }
    dialog.show()
    val result = channel.receive()
    channel.close()
    return result
}

suspend fun AppCompatActivity.showAlertDialogSuspend(
    title: String,
    message: String,
    negative: String = "取消",
    positive: String = "确定"
): Boolean? {
    val channel = Channel<Boolean?>()

    val dialog = AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)

        setNegativeButton(negative) { dialog, _ ->
            lifecycle.coroutineScope.launch {
                channel.send(false)
            }
            dialog.dismiss()
        }

        setPositiveButton(positive) { dialog, _ ->
            lifecycle.coroutineScope.launch {
                channel.send(true)
            }
            dialog.dismiss()
        }

        setOnDismissListener {
            lifecycle.coroutineScope.launch {
                kotlin.runCatching {
                    channel.send(null)
                }
            }
        }
    }

    dialog.show()
    val result = channel.receive()
    channel.close()
    return result
}