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

    fun i(tag: String = DEFAULT_TAG, msg: String?) {
        if (LOG.canI()) {
            Log.i(tag, msg ?: "nullStr")
        }
    }

    fun d(tag: String = DEFAULT_TAG, msg: String?) {
        if (LOG.canD()) {
            Log.d(tag, msg ?: "nullStr")
        }
    }

    fun w(tag: String = DEFAULT_TAG, msg: String?) {
        if (LOG.canW()) {
            Log.w(tag, msg ?: "nullStr")
        }
    }

    fun e(tag: String = DEFAULT_TAG, msg: String?) {
        if (LOG.canE()) {
            Log.e(tag, msg ?: "nullStr")
        }
    }
}