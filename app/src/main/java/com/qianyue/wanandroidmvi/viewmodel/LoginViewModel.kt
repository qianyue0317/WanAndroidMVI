package com.qianyue.wanandroidmvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.LoginUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.LoginUiState
import com.qianyue.wanandroidmvi.user.User
import kotlinx.coroutines.launch

/**
 * @author QianYue
 * @since 2023/8/30
 */
class LoginViewModel(private var _isLogin: Boolean = true) :
    BaseViewModel<LoginUiIntent, LoginUiState>() {

    val isLogin get() = _isLogin

    override fun initState(): LoginUiState {
        _isLogin = true
        return LoginUiState.InitState(_isLogin)
    }

    override suspend fun processIntent(uiIntent: LoginUiIntent) {
        when (uiIntent) {
            is LoginUiIntent.ChangeModeIntent -> {
                _isLogin = !_isLogin
                sendUiState { LoginUiState.ModeChangeState(_isLogin) }
            }

            is LoginUiIntent.Submit -> {
                if (_isLogin) {
                    val loginRes =
                        request({ if (it is RequestState.BeforeRequest) sendUiState { LoginUiState.ShowLoading() } }) {
                            API_SERVICE.login(
                                uiIntent.userName,
                                uiIntent.password
                            )
                        }
                    if (loginRes.isSuccessful()) {
                        User.loginSuccess(loginRes.data!!)
                        sendUiState { LoginUiState.LoginResult(true) }
                    } else {
                        sendUiState { LoginUiState.LoginResult(false, loginRes.errorMsg) }
                    }
                } else {

                }
            }
        }
    }
}