package com.qianyue.wanandroidmvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.mycollected.MyCollectedArticleFragment
import com.qianyue.wanandroidmvi.ui.uiintent.MyCollectedIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyCollectedState
import kotlinx.coroutines.launch

/**
 * @author QianYue
 * @since 2023/8/31
 */
class MyCollectedViewModel : BaseViewModel<MyCollectedIntent, MyCollectedState>() {

    private var _currentIndex = 0

    override fun initState(): MyCollectedState = MyCollectedState.Init()

    override suspend fun processIntent(uiIntent: MyCollectedIntent) {
        when (uiIntent) {
            is MyCollectedIntent.RefreshArticleData -> {
                _currentIndex = 0
                if (uiIntent.type == MyCollectedArticleFragment.COLLECT_TYPE_ARTICLE) {
                    val res = request { API_SERVICE.getCollectedArticleList(_currentIndex) }
                    res.apply { sendUiState { MyCollectedState.OnRefreshArticleList((this@apply).data?.datas) } }
                } else {

                }
            }

            is MyCollectedIntent.LoadMoreArticleData -> {
                _currentIndex++
                if (uiIntent.type == MyCollectedArticleFragment.COLLECT_TYPE_ARTICLE) {
                    val res = request { API_SERVICE.getCollectedArticleList(_currentIndex) }
                    res.apply { sendUiState { MyCollectedState.OnLoadMoreArticleList((this@apply).data?.datas) } }
                }
            }
        }
    }
}