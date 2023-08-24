@file:Suppress("MemberVisibilityCanBePrivate")

package com.qianyue.wanandroidmvi.constant

/**
 * @author QianYue
 * @since 2023/8/15
 */

object NetworkConstant {
    const val SUCCESS = 0
    const val EXCEPTION = -12345

    const val UNKNOWN_ERROR = "unknown_error"
}

object DEBUGGABLE {
    const val DEBUG = true
}

object LOG {
    const val LOG_LEVEL = 5

    const val LOG_I = 4
    const val LOG_D = 3
    const val LOG_W = 2
    const val LOG_E = 1

    fun canI() = LOG_LEVEL >= LOG_I

    fun canD() = LOG_LEVEL >= LOG_D

    fun canW() = LOG_LEVEL >= LOG_W

    fun canE() = LOG_LEVEL >= LOG_E
}