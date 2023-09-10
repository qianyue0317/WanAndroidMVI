package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.bean.WebAddressItem

/**
 * @author QianYue
 * @since 2023/8/31
 */
sealed class MyCollectedState: IUiState {
    class Init : MyCollectedState()

    // <editor-fold desc="收藏文章相关">
    class OnRefreshArticleList(var list: List<ArticleItem>?): MyCollectedState()
    class OnLoadMoreArticleList(var list: List<ArticleItem>?): MyCollectedState()
    // </editor-fold>

    // <editor-fold desc="收藏网站相关">
    class OnRefreshWebAddressList(var list: List<WebAddressItem>?): MyCollectedState()
    class OnLoadMoreWebAddressList()
    // </editor-fold>

    class ChangeLastPageState(val isListPage: Boolean): MyCollectedState()
    @Suppress("SpellCheckingInspection")
    class UncollectResult(val successful: Boolean, val errorMsg: String, val position: Int) : MyCollectedState()
}