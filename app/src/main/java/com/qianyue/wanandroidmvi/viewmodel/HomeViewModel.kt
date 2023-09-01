package com.qianyue.wanandroidmvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.HomeUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.HomeUiState
import com.qianyue.wanandroidmvi.utils.WanLog
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : BaseViewModel<HomeUiIntent, HomeUiState>() {

    private var _currPageIndex = 0

    override fun initState(): HomeUiState = HomeUiState.Init()

    override suspend fun processIntent(uiIntent: HomeUiIntent) {
        when (uiIntent) {
            is HomeUiIntent.RefreshIntent -> {
                val response = request { API_SERVICE.getBanner() }
                val bannerData = response.takeIf { it.isSuccessful() }?.data
                _currPageIndex = 0
                val articleList = requestArticle()
                sendUiState { HomeUiState.RefreshState(bannerData, articleList) }
            }

            is HomeUiIntent.LoadMore -> {
                _currPageIndex++
                val articleList = requestArticle()
                sendUiState { HomeUiState.LoadMoreState(articleList) }
            }
        }
    }

    private suspend fun requestArticle(): List<ArticleItem>? {
        var resultList: List<ArticleItem>? = null
        val response = request { API_SERVICE.getArticleList(_currPageIndex) }
        if (_currPageIndex == 0) {
            resultList = request { API_SERVICE.getTopArticleList() }.data
        }
        resultList = resultList?.toMutableList()?.apply {
            addAll(
                response.data?.datas ?: emptyList()
            )
        }
            ?: response.data?.datas
        return resultList
    }

}