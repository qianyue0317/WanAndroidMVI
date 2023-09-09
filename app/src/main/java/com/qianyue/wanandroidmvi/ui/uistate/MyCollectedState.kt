package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ArticleItem

/**
 * @author QianYue
 * @since 2023/8/31
 */
sealed class MyCollectedState: IUiState {
    class Init : MyCollectedState()
    class OnRefreshArticleList(var list: List<ArticleItem>? = null): MyCollectedState()
    class OnLoadMoreArticleList(var list: List<ArticleItem>? = null): MyCollectedState()

    class ChangeLastPageState(val isListPage: Boolean): MyCollectedState()
    class UncollectResult(val successful: Boolean, val errorMsg: String, val position: Int) : MyCollectedState()
}