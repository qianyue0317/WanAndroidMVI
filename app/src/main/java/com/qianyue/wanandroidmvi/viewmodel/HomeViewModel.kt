package com.qianyue.wanandroidmvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.HomeUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.HomeUiState
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : BaseViewModel<HomeUiIntent, HomeUiState>() {

    private var _currPageIndex = 0

    override fun initState(): HomeUiState = HomeUiState.RefreshState()

    override fun processIntent(uiIntent: HomeUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                is HomeUiIntent.RefreshIntent -> {
                    val response = request { API_SERVICE.getBanner() }
                    response.takeIf { it.isSuccessful() }
                        ?.apply { sendUiState { HomeUiState.LoadBanner(this@apply.data) } }


                    _currPageIndex = 0
                    requestArticle {
                        HomeUiState.RefreshState(it)
                    }

                }

                is HomeUiIntent.LoadMore -> {
                    _currPageIndex++
                    requestArticle {
                        HomeUiState.LoadMoreState(it)
                    }
                }
            }
        }
    }

    private suspend fun requestArticle(stateInitial: (List<ArticleItem>?) -> HomeUiState) {
        var resultList: List<ArticleItem>? = null
        val response = API_SERVICE.getArticleList(_currPageIndex)
        response.takeUnless { it.isSuccessful() }?.apply {
            sendUiState { HomeUiState.ErrorState }
            return
        }
        if (_currPageIndex == 0) {
            resultList = API_SERVICE.getTopArticleList().data
        }
        resultList = resultList?.toMutableList()?.apply {
            addAll(
                response.data?.datas ?: emptyList()
            )
        }
            ?: response.data?.datas
        sendUiState {
            stateInitial(resultList)
        }
    }

}