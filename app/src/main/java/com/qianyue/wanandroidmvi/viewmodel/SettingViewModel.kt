package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.SettingUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.SettingUiState
import com.qianyue.wanandroidmvi.user.User

/**
 * @author QianYue
 * @since 2023/9/19
 */
class SettingViewModel: BaseViewModel<SettingUiIntent, SettingUiState>() {
    override fun initState(): SettingUiState = SettingUiState.Init

    override suspend fun processIntent(uiIntent: SettingUiIntent) {
        when (uiIntent) {
            is SettingUiIntent.Logout -> {
                val res = request { API_SERVICE.logout() }
                if (res.isSuccessful()) {
                    User.logout()
                    sendUiState { SettingUiState.OnLogout() }
                }
            }
        }
    }
}