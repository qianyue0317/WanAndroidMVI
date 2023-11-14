package com.qianyue.wanandroidmvi.viewmodel

import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.constant.NetworkConstant
import com.qianyue.wanandroidmvi.model.network.AppResponse
import com.qianyue.wanandroidmvi.user.User

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

    if (result.getOrNull()?.errorCode == NetworkConstant.TOKEN_INVALIDATE) {
        User.logout()
        Toaster.show("登录已过期！")
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

