package com.qianyue.wanandroidmvi.model.network

import com.qianyue.wanandroidmvi.constant.NetworkConstant

/**
 * @author QianYue
 * @since 2023/8/15
 */
@Suppress("MemberVisibilityCanBePrivate")
class AppResponse<T>(val errorCode: Int, val errorMsg: String, val data: T?) {
    fun isSuccessful() = errorCode == NetworkConstant.SUCCESS
}