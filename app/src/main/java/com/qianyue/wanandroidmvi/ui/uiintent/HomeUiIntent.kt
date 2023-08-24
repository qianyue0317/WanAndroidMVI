package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/8/15
 */
sealed class HomeUiIntent: IUiIntent {
    object RefreshIntent : HomeUiIntent()

    object LoadMore: HomeUiIntent()
}