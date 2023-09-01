package com.qianyue.wanandroidmvi.widgets

import android.graphics.Outline
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.TextView
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

fun View.circleOutline() {
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            view?.let {
                if (it.height != it.width) throw RuntimeException("require with equals height")
                outline?.setRoundRect(0, 0, it.width, it.height, it.width / 2f)
            }
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

fun TextView.filterChineseChar() {
    filters = arrayOf(object : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {

            for (i in start until end) {
                val tempChar = source?.get(i)
                if ((tempChar?.code ?: 0) >= 0x4e00 && (tempChar?.code ?: 0) <= 0x9fa5) {
                    return ""
                }
            }
            return null
        }

    })
}

fun ViewGroup.foreachChild(block: (View) -> Unit) {
    for (i in 0 until childCount) {
        block(getChildAt(i))
    }
}
