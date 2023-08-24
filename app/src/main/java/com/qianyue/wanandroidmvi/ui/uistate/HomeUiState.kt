package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.bean.BannerItem

/**
 * @author QianYue
 * @since 2023/8/15
 */
sealed class HomeUiState: IUiState {
    class RefreshState(val articleList: List<ArticleItem>? = null): HomeUiState()
    class LoadMoreState(val articleList: List<ArticleItem>? = null): HomeUiState()
    class LoadBanner(val bannerList: List<BannerItem>? = null): HomeUiState()
    object ErrorState : HomeUiState()
}