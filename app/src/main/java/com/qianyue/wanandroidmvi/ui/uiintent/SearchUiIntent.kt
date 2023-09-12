package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/9/10
 */
sealed class SearchUiIntent : IUiIntent {
    // <editor-fold desc="搜索页">
    class GetWords : SearchUiIntent()
    // </editor-fold>

    // <editor-fold desc="搜索结果页">
    class Refresh : SearchUiIntent()
    class LoadMore : SearchUiIntent()
    class SaveHistory(val keyword: String) : SearchUiIntent()
    class LoadHistory : SearchUiIntent()
    class SaveHistoryLocal : SearchUiIntent()
    class ClearHistory : SearchUiIntent()
    class RemoveKeyword(val pos: Int) : SearchUiIntent()
    class CollectOperate(val collect: Boolean, val id: Int): SearchUiIntent()
    // </editor-fold>
}