package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/8/31
 */
sealed class MyCollectedIntent: IUiIntent {
    // <editor-fold desc="收藏的文章列表">
    class RefreshArticleData(): MyCollectedIntent()
    class LoadMoreArticleData(): MyCollectedIntent()
    // </editor-fold>

    // <editor-fold desc="收藏的网站列表">
    class RefreshWebAddressData(): MyCollectedIntent()
    // </editor-fold>

    class UncollectArticle(val id: Int) : MyCollectedIntent()
    class UncollectWebAddress(val id: Int) : MyCollectedIntent()
}