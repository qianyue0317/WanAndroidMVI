package com.qianyue.wanandroidmvi.utils

import android.util.Log
import com.qianyue.wanandroidmvi.constant.DEBUGGABLE
import com.qianyue.wanandroidmvi.constant.LOG

/**
 * 日志打印，打印开关在appconstants中的LOG中，设置LOG_LEVEL
 *
 * @author QianYue
 * @since 2023/8/16
 */
object WanLog {
    const val DEFAULT_TAG = "wan_app_log"

    @JvmStatic
    fun i(tag: String = DEFAULT_TAG, msg: Any?) {
        if (LOG.canI()) {
            Log.i(tag, msg?.toString() ?: "nullStr")
        }
    }

    @JvmStatic
    fun d(tag: String = DEFAULT_TAG, msg: Any?) {
        if (LOG.canD()) {
            Log.d(tag, msg?.toString() ?: "nullStr")
        }
    }

    @JvmStatic
    fun w(tag: String = DEFAULT_TAG, msg: Any?) {
        if (LOG.canW()) {
            Log.w(tag, msg?.toString() ?: "nullStr")
        }
    }

    @JvmStatic
    fun e(tag: String = DEFAULT_TAG, msg: Any?) {
        if (LOG.canE()) {
            Log.e(tag, msg?.toString() ?: "nullStr")
        }
    }
}