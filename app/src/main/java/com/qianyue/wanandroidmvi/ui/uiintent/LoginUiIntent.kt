package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/8/30
 */
sealed class LoginUiIntent: IUiIntent {
    class ChangeModeIntent(): LoginUiIntent()
    class Submit(val userName: String, val password: String, val repassword: String? = null): LoginUiIntent()
}