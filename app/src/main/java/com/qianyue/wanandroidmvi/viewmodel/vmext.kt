package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.constant.NetworkConstant
import com.qianyue.wanandroidmvi.model.network.AppResponse

/**
 * @author QianYue
 * @since 2023/8/16
 */

// 请求状态
sealed class RequestState {
    object BeforeRequest : RequestState()
    object AfterRequest : RequestState()
}


/**
 * 请求接口数据
 *
 * @param block 请求的挂起函数
 * @param requestStateListener 请求状态监听，用来显示loading
 */
suspend fun <T : BaseViewModel<*, *>, R> T.request(
    requestStateListener: ((RequestState) -> Unit)? = null,
    block: suspend () -> AppResponse<R>
): AppResponse<R> {
    val result = kotlin.runCatching {
        requestStateListener?.invoke(RequestState.BeforeRequest)
        block()
    }.onSuccess {
        requestStateListener?.invoke(RequestState.AfterRequest)
    }.onFailure {
        requestStateListener?.invoke(RequestState.AfterRequest)
    }
    if (result.isSuccess) {
        return result.getOrNull()!!
    }
    return AppResponse(
        NetworkConstant.EXCEPTION,
        result.exceptionOrNull()?.message ?: NetworkConstant.UNKNOWN_ERROR,
        null
    )
}

