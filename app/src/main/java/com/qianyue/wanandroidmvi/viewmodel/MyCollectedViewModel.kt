package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.bean.AppListData
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.model.network.AppResponse
import com.qianyue.wanandroidmvi.ui.mycollected.MyCollectedArticleFragment
import com.qianyue.wanandroidmvi.ui.uiintent.MyCollectedIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyCollectedState

/**
 * @author QianYue
 * @since 2023/8/31
 */
class MyCollectedViewModel : BaseViewModel<MyCollectedIntent, MyCollectedState>() {

    private var _currentIndex = 0

    private var _isLastPage: Boolean? = null

    private val _cacheList: MutableList<ArticleItem> = mutableListOf()

    override fun initState(): MyCollectedState = MyCollectedState.Init()

    override suspend fun processIntent(uiIntent: MyCollectedIntent) {
        when (uiIntent) {
            is MyCollectedIntent.RefreshArticleData -> {
                _currentIndex = 0
                if (uiIntent.type == MyCollectedArticleFragment.COLLECT_TYPE_ARTICLE) {
                    val res = request { API_SERVICE.getCollectedArticleList(_currentIndex) }
                    checkLastPageAndSetCollect(res)
                    _cacheList.clear().run { _cacheList.addAll(res.data?.datas ?: emptyList()) }
                    res.apply { sendUiState { MyCollectedState.OnRefreshArticleList((this@apply).data?.datas) } }
                } else {

                }
            }

            is MyCollectedIntent.LoadMoreArticleData -> {
                _currentIndex++
                if (uiIntent.type == MyCollectedArticleFragment.COLLECT_TYPE_ARTICLE) {
                    val res = request { API_SERVICE.getCollectedArticleList(_currentIndex) }
                    checkLastPageAndSetCollect(res)
                    _cacheList.addAll(res.data?.datas ?: emptyList())
                    res.apply { sendUiState { MyCollectedState.OnLoadMoreArticleList((this@apply).data?.datas) } }
                }
            }

            is MyCollectedIntent.Uncollect -> {
                val res = request { API_SERVICE.uncollectArticle(uiIntent.id) }
                var position = -1
                if (res.isSuccessful()) {
                    val articleItem = _cacheList.withIndex().find { it.value.originId == uiIntent.id }
                    position = articleItem?.index ?: -1
                    articleItem?.value?.collect = false
                    position.takeIf { it >= 0 }?.let { _cacheList.removeAt(it) }
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