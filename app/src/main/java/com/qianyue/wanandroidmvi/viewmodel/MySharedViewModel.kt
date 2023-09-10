package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.MySharedUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MySharedUiState

/**
 * @author QianYue
 * @since 2023/9/9
 */
class MySharedViewModel: BaseViewModel<MySharedUiIntent, MySharedUiState>() {
    private var _currentIndex = 1

    override fun initState(): MySharedUiState = MySharedUiState.Init()

    override suspend fun processIntent(uiIntent: MySharedUiIntent) {
        when (uiIntent) {
            is MySharedUiIntent.Refresh -> {
                _currentIndex = 1
                val res = request { API_SERVICE.getMySharedArticleList(_currentIndex) }
                res.takeIf { it.isSuccessful() }
                    ?.also { sendUiState {
                        MySharedUiState.OnRefresh(
                            it.data?.shareArticles?.datas,
                            (it.data?.shareArticles?.pageCount ?: 0) == _currentIndex
                        ) } }
                    ?: sendUiState { MySharedUiState.RefreshError() }
            }

            is MySharedUiIntent.LoadMore -> {
                val res = request { API_SERVICE.getMySharedArticleList(++_currentIndex) }
                res.takeIf { it.isSuccessful() }
                    ?.also { sendUiState {
                        MySharedUiState.OnLoadMore(
                            it.data?.shareArticles?.datas,
                            (it.data?.shareArticles?.pageCount ?: 0) == _currentIndex
                        ) } }
                    ?: sendUiState { MySharedUiState.LoadMoreError() }
            }

            is MySharedUiIntent.Share -> {
                val res = request { API_SERVICE.shareArticle(uiIntent.title, uiIntent.link) }
                sendUiState { MySharedUiState.ShareResult(res.isSuccessful(), res.errorMsg) }
            }

            is MySharedUiIntent.DeleteShareArticle -> {
                val res = request { API_SERVICE.deleteSharedArticle(uiIntent.id) }
                sendUiState { MySharedUiState.DeleteResult(res.isSuccessful(), res.errorMsg, uiIntent.id) }
            }
        }
    }
}