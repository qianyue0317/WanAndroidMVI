package com.qianyue.wanandroidmvi.widgets

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.qianyue.wanandroidmvi.WanApplication

/**
 * @author QianYue
 * @since 2023/8/28
 */

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