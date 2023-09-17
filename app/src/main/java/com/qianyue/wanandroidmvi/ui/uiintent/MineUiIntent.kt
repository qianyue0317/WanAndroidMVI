package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/8/23
 */
sealed class MineUiIntent: IUiIntent {
    class GetUserCoin(): MineUiIntent()
}