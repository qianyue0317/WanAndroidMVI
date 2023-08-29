package com.qianyue.wanandroidmvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.PlazaUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.PlazaUiState
import kotlinx.coroutines.launch

/**
 * 广场ViewModel
 */
class PlazaViewModel : BaseViewModel<PlazaUiIntent, PlazaUiState>() {
    private var _currentIndex = 1
    override fun initState(): PlazaUiState = PlazaUiState.InitState()

    override fun processIntent(uiIntent: PlazaUiIntent) {
        viewModelScope.launch {
            when (uiIntent) {
                is PlazaUiIntent.RefreshIntent -> {
                    _currentIndex = 1
                    val response = request { API_SERVICE.getPlazaArticleList(_currentIndex) }
                    response.takeIf { it.isSuccessful() }
                        ?.let { sendUiState { PlazaUiState.OnRefreshState(it.data?.datas) } }
                }

                is PlazaUiIntent.LoadMoreIntent -> {
                    _currentIndex++
                    val response = request { API_SERVICE.getPlazaArticleList(_currentIndex) }
                    response.takeIf { it.isSuccessful() }
                        ?.let { sendUiState { PlazaUiState.OnLoadMoreState(it.data?.datas) } }
                }
            }
        }
    }
}