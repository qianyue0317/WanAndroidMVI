package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.bean.BannerItem

/**
 * @author QianYue
 * @since 2023/8/15
 */
sealed class HomeUiState: IUiState {
    class Init(): HomeUiState()
    class RefreshState(val bannerList: List<BannerItem>? = null, val articleList: List<ArticleItem>? = null): HomeUiState()
    class LoadMoreState(val articleList: List<ArticleItem>? = null): HomeUiState()
    class CollectResult(val successful: Boolean,val errorMsg: String, val position: Int = -1) : HomeUiState()

    object ErrorState : HomeUiState()
}