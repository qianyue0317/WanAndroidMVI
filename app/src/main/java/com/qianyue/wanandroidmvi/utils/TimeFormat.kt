package com.qianyue.wanandroidmvi.utils

import java.lang.Exception
import java.text.DateFormat
import java.util.Date

/**
 * @author QianYue
 * @since 2023/8/23
 */
object TimeFormat {
    private val dateTimeFormat = DateFormat.getDateTimeInstance()
    private val dateFormat = DateFormat.getDateTimeInstance()

    fun formatDateTime(date: Long?): String {
        if (date == null) {
            return ""
        }
        return try {
            dateTimeFormat.format(Date(date))
        } catch (e: Exception) {
            ""
        }
    }

    fun formatDate(date: Long?) : String {
        if (date == null) {
            return ""
        }
        return try {
            dateFormat.format(Date(date))
        } catch (e: Exception) {
            ""
        }
    }
}