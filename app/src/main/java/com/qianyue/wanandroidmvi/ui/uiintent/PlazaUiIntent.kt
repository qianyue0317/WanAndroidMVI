package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/8/15
 */
sealed class PlazaUiIntent: IUiIntent {
    class RefreshIntent: PlazaUiIntent()
    class LoadMoreIntent : PlazaUiIntent()
    class CollectOperate(val collect: Boolean, val id: Int) : PlazaUiIntent()
}