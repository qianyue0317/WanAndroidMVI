package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ArticleItem

/**
* @author QianYue
* @since 2023/8/15
*/
sealed class PlazaUiState: IUiState {
    class InitState(): PlazaUiState()
    class OnRefreshState(val list: List<ArticleItem>? = null) : PlazaUiState()

    class OnLoadMoreState(val list: List<ArticleItem>? = null): PlazaUiState()
}