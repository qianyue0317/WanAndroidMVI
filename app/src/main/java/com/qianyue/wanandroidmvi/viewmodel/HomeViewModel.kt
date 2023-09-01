package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.HomeUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.HomeUiState

class HomeViewModel : BaseViewModel<HomeUiIntent, HomeUiState>() {

    private var _currPageIndex = 0

    private val cacheList: MutableList<ArticleItem> = mutableListOf()

    override fun initState(): HomeUiState = HomeUiState.Init()

    override suspend fun processIntent(uiIntent: HomeUiIntent) {
        when (uiIntent) {
            is HomeUiIntent.RefreshIntent -> {
                val response = request { API_SERVICE.getBanner() }
                val bannerData = response.takeIf { it.isSuccessful() }?.data
                _currPageIndex = 0
                val articleList = requestArticle()
                cacheList.clear().run { cacheList.addAll(articleList ?: emptyList()) }
                sendUiState { HomeUiState.RefreshState(bannerData, articleList) }
            }

            is HomeUiIntent.LoadMore -> {
                _currPageIndex++
                val articleList = requestArticle()
                cacheList.addAll(articleList ?: emptyList())
                sendUiState { HomeUiState.LoadMoreState(articleList) }
            }

            is HomeUiIntent.CollectOperate -> {
                val res = request {
                    if (uiIntent.collect) API_SERVICE.collectArticle(uiIntent.id) else API_SERVICE.uncollectArticle(
                        uiIntent.id
                    )
                }
                var position = -1
                if (res.isSuccessful()) {
                    val articleItem = cacheList.withIndex().find { it.value.id == uiIntent.id }
                    position = articleItem?.index ?: -1
                    articleItem?.value?.collect = !(articleItem?.value?.collect ?: true)
                }
                sendUiState {
                    HomeUiState.CollectResult(
                        res.isSuccessful(),
                        res.errorMsg,
                        position
                    )
                }
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