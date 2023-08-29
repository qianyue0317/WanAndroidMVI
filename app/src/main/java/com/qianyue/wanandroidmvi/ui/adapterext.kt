package com.qianyue.wanandroidmvi.ui

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @author QianYue
 * @since 2023/8/29
 */

// 可以添加可空集合
fun <T, VH : RecyclerView.ViewHolder> BaseQuickAdapter<T, VH>.safeAddAll(collection: Collection<T>?) =
    collection?.takeUnless { it.isEmpty() }?.let { addAll(it) }

