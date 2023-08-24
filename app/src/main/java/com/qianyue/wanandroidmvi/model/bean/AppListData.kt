package com.qianyue.wanandroidmvi.model.bean

/**
 * @author QianYue
 * @since 2023/8/15
 */
data class AppListData<T>(
    var datas: List<T>,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)
