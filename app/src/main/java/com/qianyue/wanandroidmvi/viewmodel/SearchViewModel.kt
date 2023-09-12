package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.SearchUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.HomeUiState
import com.qianyue.wanandroidmvi.ui.uistate.SearchUiState
import com.tencent.mmkv.MMKV

/**
 * @author QianYue
 * @since 2023/9/10
 */
class SearchViewModel : BaseViewModel<SearchUiIntent, SearchUiState>() {

    private val _cacheList: MutableList<ArticleItem> = mutableListOf()

    private var _currentIndex = 0

    var keyWord: String? = null

    private val historyKeywordList = mutableListOf<String>()

    companion object {
        const val SEARCH_MMKV = "search_mmkv"

        const val KEY_WORD_SPLIT_TAG = "__==++&&**"
    }

    override fun initState(): SearchUiState = SearchUiState.Init

    override suspend fun processIntent(uiIntent: SearchUiIntent) {
        when (uiIntent) {
            is SearchUiIntent.GetWords -> {
                val res = request { API_SERVICE.getHotKeyWords() }
                sendUiState { SearchUiState.WordsResult(res.data) }
            }

            // <editor-fold desc="搜索结果页的功能">
            is SearchUiIntent.LoadHistory -> {
                val mmkv = MMKV.mmkvWithID(SEARCH_MMKV)
                val resultString = mmkv.decodeString(SEARCH_MMKV, "")
                resultString?.split(KEY_WORD_SPLIT_TAG)?.let { historyKeywordList.addAll(it) }
                sendUiState { SearchUiState.OnLoadHistoryWord(historyKeywordList) }
            }

            is SearchUiIntent.Refresh -> {
                keyWord ?: return
                _currentIndex = 0
                val res = request { API_SERVICE.searchForKeyWord(_currentIndex, keyWord!!) }
                _cacheList.clear()
                _cacheList.addAll(res.data?.datas ?: emptyList())
                sendUiState { SearchUiState.OnRefresh(res.data?.datas) }
            }

            is SearchUiIntent.LoadMore -> {
                _currentIndex++
                val res = request { API_SERVICE.searchForKeyWord(_currentIndex, keyWord!!) }
                _cacheList.addAll(res.data?.datas ?: emptyList())
                sendUiState { SearchUiState.OnLoadMore(res.data?.datas) }
            }

            is SearchUiIntent.SaveHistory -> {
                var fromPos = -1
                if (historyKeywordList.contains(uiIntent.keyword)) {
                    fromPos = historyKeywordList.indexOf(uiIntent.keyword)
                    if (fromPos == 0) return
                    historyKeywordList.remove(uiIntent.keyword)
                }
                historyKeywordList.add(0, uiIntent.keyword)
                sendUiState { SearchUiState.OnSaveHistory(0, fromPos) }
            }

            is SearchUiIntent.SaveHistoryLocal -> {
                val result = historyKeywordList.joinToString(separator = KEY_WORD_SPLIT_TAG)
                val mmkv = MMKV.mmkvWithID(SEARCH_MMKV)
                mmkv.encode(SEARCH_MMKV, result)
            }

            is SearchUiIntent.ClearHistory -> {
                historyKeywordList.clear()
                sendUiState { SearchUiState.OnClearHistory() }
            }

            is SearchUiIntent.RemoveKeyword -> {
                historyKeywordList.removeAt(uiIntent.pos)
                sendUiState { SearchUiState.OnDeleteKeyword(uiIntent.pos) }
            }

            is SearchUiIntent.CollectOperate -> {
                val res = request {
                    if (uiIntent.collect) API_SERVICE.collectArticle(uiIntent.id) else API_SERVICE.uncollectArticle(
                        uiIntent.id
                    )
                }
                var position = -1
                if (res.isSuccessful()) {
                    val articleItem = _cacheList.withIndex().find { it.value.id == uiIntent.id }
                    position = articleItem?.index ?: -1
                    articleItem?.value?.collect = !(articleItem?.value?.collect ?: true)
                }
                sendUiState {
                    SearchUiState.CollectResult(
                        res.isSuccessful(),
                        res.errorMsg,
                        position
                    )
                }
            }
            // </editor-fold>
        }
    }
}