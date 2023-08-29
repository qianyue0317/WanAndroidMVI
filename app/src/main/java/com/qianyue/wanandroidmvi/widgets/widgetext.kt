package com.qianyue.wanandroidmvi.widgets

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.qianyue.wanandroidmvi.WanApplication
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**
 * @author QianYue
 * @since 2023/8/28
 */

fun View.setSafeClickListener(onClick: (View)-> Unit) {
    var lastTime = 0L
    setOnClickListener {
        if (System.currentTimeMillis() - lastTime < 1000) {
            return@setOnClickListener
        }
        lastTime = System.currentTimeMillis()
        onClick(it)
    }
}

/**
 * 设置圆角
 */
fun View.roundOutLine(radius: Float) {
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            view?.let { outline?.setRoundRect(0, 0, it.width, it.height, radius) }
        }
    }
}

fun View.dp2px(dp: Float): Float {
    return resources.displayMetrics.density * dp
}

fun Float.dp2px(): Float {
    return WanApplication.instance.resources.displayMetrics.density * this
}

/**
 * refreshLayout设置经典header和footer
 */
fun SmartRefreshLayout.classicConfig(
    onRefresh: (RefreshLayout) -> Unit,
    onLoadMore: (RefreshLayout) -> Unit
) {
    setEnableRefresh(true)
    setEnableLoadMore(true)
    setRefreshHeader(ClassicsHeader(context))
    setRefreshFooter(ClassicsFooter(context))
    setOnRefreshListener(onRefresh)
    setOnLoadMoreListener(onLoadMore)
}