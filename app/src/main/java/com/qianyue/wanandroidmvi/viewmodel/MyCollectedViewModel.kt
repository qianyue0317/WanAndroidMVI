package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.bean.AppListData
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.bean.WebAddressItem
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.model.network.AppResponse
import com.qianyue.wanandroidmvi.ui.uiintent.MyCollectedIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyCollectedState

/**
 * @author QianYue
 * @since 2023/8/31
 */
class MyCollectedViewModel : BaseViewModel<MyCollectedIntent, MyCollectedState>() {

    private var _currentIndex = 0

    private var _isLastPage: Boolean? = null

    private val _cacheArticleList: MutableList<ArticleItem> = mutableListOf()

    private val _cacheWebAddressList : MutableList<WebAddressItem> = mutableListOf()

    override fun initState(): MyCollectedState = MyCollectedState.Init()

    override suspend fun processIntent(uiIntent: MyCollectedIntent) {
        when (uiIntent) {
            is MyCollectedIntent.RefreshArticleData -> {
                _currentIndex = 0
                val res = request { API_SERVICE.getCollectedArticleList(_currentIndex) }
                checkLastPageAndSetCollect(res)
                _cacheArticleList.clear().run { _cacheArticleList.addAll(res.data?.datas ?: emptyList()) }
                res.apply { sendUiState { MyCollectedState.OnRefreshArticleList((this@apply).data?.datas) } }
            }

            is MyCollectedIntent.LoadMoreArticleData -> {
                _currentIndex++
                val res = request { API_SERVICE.getCollectedArticleList(_currentIndex) }
                checkLastPageAndSetCollect(res)
                _cacheArticleList.addAll(res.data?.datas ?: emptyList())
                res.apply { sendUiState { MyCollectedState.OnLoadMoreArticleList((this@apply).data?.datas) } }
            }

            is MyCollectedIntent.RefreshWebAddressData -> {
                val res = request { API_SERVICE.getCollectedWebAddressList() }
                _cacheWebAddressList.clear()
                _cacheWebAddressList.addAll(res.data ?: emptyList())
                res.apply { sendUiState { MyCollectedState.OnRefreshWebAddressList(res.data) } }
            }

            is MyCollectedIntent.UncollectArticle -> {
                val res = request { API_SERVICE.uncollectArticle(uiIntent.id) }
                var position = -1
                if (res.isSuccessful()) {
                    val articleItem = _cacheArticleList.withIndex().find { it.value.originId == uiIntent.id }
                    position = articleItem?.index ?: -1
                    articleItem?.value?.collect = false
                    position.takeIf { it >= 0 }?.let { _cacheArticleList.removeAt(it) }
                }
                sendUiState {
                    MyCollectedState.UncollectResult(
                        res.isSuccessful(),
                        res.errorMsg,
                        position
                    )
                }
            }

            is MyCollectedIntent.UncollectWebAddress -> {
                val res = request { API_SERVICE.uncollectTool(uiIntent.id) }
                var position = -1
                if (res.isSuccessful()) {
                    val articleItem = _cacheWebAddressList.withIndex().find { it.value.id == uiIntent.id }
                    position = articleItem?.index ?: -1
                    position.takeIf { it >= 0 }?.let { _cacheWebAddressList.removeAt(it) }
                }
                sendUiState {
                    MyCollectedState.UncollectResult(
                        res.isSuccessful(),
                        res.errorMsg,
                        position
                    )
                }
            }
        }
    }

    private fun checkLastPageAndSetCollect(res: AppResponse<AppListData<ArticleItem>>) {
        val lastPage =
            res.takeIf { it.isSuccessful() }?.data?.let { it.pageCount == it.curPage } ?: false
        if (lastPage != _isLastPage) {
            _isLastPage = lastPage
            sendUiState { MyCollectedState.ChangeLastPageState(lastPage) }
        }

        // 收藏页面返回的数据没有collect字段，这里为了使用的是复用的bean和adapter，在本地将collect设置为true
        res.data?.datas?.forEach {
            it.collect = true
        }
    }
}