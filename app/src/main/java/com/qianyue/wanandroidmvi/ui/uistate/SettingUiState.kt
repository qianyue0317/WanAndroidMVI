package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState

/**
 * @author QianYue
 * @since 2023/9/19
 */
sealed class SettingUiState: IUiState {
    object Init: SettingUiState()
    class OnLogout : SettingUiState()
}