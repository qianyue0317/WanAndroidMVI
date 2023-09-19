package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/9/19
 */
sealed class SettingUiIntent: IUiIntent {
    class Logout : SettingUiIntent()
}