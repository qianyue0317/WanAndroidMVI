package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/8/15
 */
sealed class HomeUiIntent: IUiIntent {
    /**
     * @param collect true 为收藏，false为取消收藏
     */
    class CollectOperate(val collect: Boolean, val id: Int) : HomeUiIntent()
    object RefreshIntent : HomeUiIntent()

    object LoadMore: HomeUiIntent()
}