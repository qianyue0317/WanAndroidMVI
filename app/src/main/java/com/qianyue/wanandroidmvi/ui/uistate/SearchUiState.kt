package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.bean.KeyWord

/**
 * @author QianYue
 * @since 2023/9/10
 */
sealed class SearchUiState: IUiState {
    // <editor-fold desc="搜索页">
    class WordsResult(val list: List<KeyWord>?) : SearchUiState()
    object Init : SearchUiState()
    // </editor-fold>

    // <editor-fold desc="搜索结果页">
    class OnRefresh(val list: List<ArticleItem>?): SearchUiState()
    class OnLoadMore(val list: List<ArticleItem>?): SearchUiState()
    class OnLoadHistoryWord(val keywordList: List<String>) : SearchUiState()
    class OnSaveHistory(val toPos: Int, val fromPos: Int) : SearchUiState()
    class OnClearHistory : SearchUiState()
    class OnDeleteKeyword(val pos: Int) : SearchUiState()
    // </editor-fold>
}