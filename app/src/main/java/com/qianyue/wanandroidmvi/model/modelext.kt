package com.qianyue.wanandroidmvi.model

import com.qianyue.wanandroidmvi.model.bean.AppListData

/**
 * @author QianYue
 * @since 2023/8/15
 */

fun AppListData<*>.isEmpty() = datas.isEmpty()

// 是否能加载更多
fun AppListData<*>.hasMore() = !over
