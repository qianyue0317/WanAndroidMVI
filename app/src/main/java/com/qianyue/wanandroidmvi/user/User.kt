package com.qianyue.wanandroidmvi.user

import com.google.gson.GsonBuilder
import com.qianyue.wanandroidmvi.model.bean.UserInfo
import com.qianyue.wanandroidmvi.utils.WanLog
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object User {
    private const val TAG = "user"

    private const val USER_KEY = "user_info_key"

    private var userInfo: UserInfo? = null

    val loginState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun loginSuccess(userInfo: UserInfo) {
        this.userInfo = userInfo
        val mmkv = MMKV.defaultMMKV()
        mmkv.encode(USER_KEY, GsonBuilder().create().toJson(userInfo))
        loginState.update { true }
    }

    fun loadCacheUser() {
        val defaultMMKV = MMKV.defaultMMKV()
        kotlin.runCatching {
            if (defaultMMKV.containsKey(USER_KEY)) {
                userInfo = GsonBuilder().create()
                    .fromJson(defaultMMKV.decodeString(USER_KEY, ""), UserInfo::class.java)
                loginState.update { true }
            }
        }.onFailure {
            WanLog.d(TAG, it.message)
        }
    }

    fun isLoginSuccess() = userInfo != null

    val userName: String get() = userInfo?.nickname ?: ""

    val coinCount: Int get() = userInfo?.coinCount ?: 0

}