package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState

/**
 * @author QianYue
 * @since 2023/8/30
 */
sealed class LoginUiState: IUiState {
    class InitState(val isLogin: Boolean): LoginUiState()
    class ModeChangeState(val isLogin: Boolean) : LoginUiState()
    class LoginResult(val success: Boolean, val msg: String? = null): LoginUiState()
    class ShowLoading(): LoginUiState()
}