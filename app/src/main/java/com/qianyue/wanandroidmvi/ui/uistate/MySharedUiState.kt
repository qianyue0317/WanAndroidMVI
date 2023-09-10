package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ArticleItem

/**
 * @author QianYue
 * @since 2023/9/9
 */
sealed class MySharedUiState: IUiState {
    class Init: MySharedUiState()
    class ShareResult(val success: Boolean, val msg: String) : MySharedUiState()
    class OnRefresh(val list: List<ArticleItem>?, val noMore: Boolean) : MySharedUiState()
    class RefreshError : MySharedUiState()
    class OnLoadMore(val list: List<ArticleItem>?, val noMore: Boolean) : MySharedUiState()
    class LoadMoreError : MySharedUiState()
    class DeleteResult(val successful: Boolean, val errorMsg: String, val id: Int) : MySharedUiState()
}