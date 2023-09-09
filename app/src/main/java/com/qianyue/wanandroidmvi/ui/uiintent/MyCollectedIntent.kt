package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent
import com.qianyue.wanandroidmvi.ui.mycollected.MyCollectedArticleFragment

/**
 * @author QianYue
 * @since 2023/8/31
 */
sealed class MyCollectedIntent: IUiIntent {
    class RefreshArticleData(@MyCollectedArticleFragment.CollectType var type: Int): MyCollectedIntent()
    class LoadMoreArticleData(@MyCollectedArticleFragment.CollectType var type: Int): MyCollectedIntent()
    class Uncollect(val id: Int) : MyCollectedIntent()
}