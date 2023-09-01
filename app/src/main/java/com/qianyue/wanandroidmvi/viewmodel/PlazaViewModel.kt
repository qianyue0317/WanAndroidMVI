package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.PlazaUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.PlazaUiState

/**
 * 广场ViewModel
 */
class PlazaViewModel : BaseViewModel<PlazaUiIntent, PlazaUiState>() {
    private var _currentIndex = 0

    private val cacheList: MutableList<ArticleItem> = mutableListOf()

    override fun initState(): PlazaUiState = PlazaUiState.InitState()

    override suspend fun processIntent(uiIntent: PlazaUiIntent) {
        when (uiIntent) {
            is PlazaUiIntent.RefreshIntent -> {
                _currentIndex = 0
                val response = request { API_SERVICE.getPlazaArticleList(_currentIndex) }
                cacheList.clear().run { cacheList.addAll(response.data?.datas ?: emptyList()) }
                response.let { sendUiState { PlazaUiState.OnRefreshState(it.data?.datas) } }
            }

            is PlazaUiIntent.LoadMoreIntent -> {
                _currentIndex++
                val response = request { API_SERVICE.getPlazaArticleList(_currentIndex) }
                cacheList.addAll(response.data?.datas ?: emptyList())
                response.let { sendUiState { PlazaUiState.OnLoadMoreState(it.data?.datas) } }
            }
            is PlazaUiIntent.CollectOperate -> {
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
                    PlazaUiState.CollectResult(
                        res.isSuccessful(),
                        res.errorMsg,
                        position
                    )
                }
            }
        }
    }
}