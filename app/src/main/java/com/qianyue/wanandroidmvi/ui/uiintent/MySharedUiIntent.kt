package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/9/9
 */
sealed class MySharedUiIntent: IUiIntent {
    class Refresh : MySharedUiIntent()
    class LoadMore: MySharedUiIntent()
    class Share(val title:String, val link: String) : MySharedUiIntent()
    class DeleteShareArticle(val id: Int) : MySharedUiIntent()
}