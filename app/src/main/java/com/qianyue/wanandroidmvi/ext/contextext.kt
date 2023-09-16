package com.qianyue.wanandroidmvi.ext

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.qianyue.wanandroidmvi.utils.WanImageLoader

/**
 * @author QianYue
 * @since 2023/8/15
 */

fun Context.dp2px(dp: Float) : Float{
    return resources.displayMetrics.density * dp
}

fun Context.loadImage(url: String?, iv: ImageView) {
    WanImageLoader.loadImage(this, url, iv)
}

@ColorInt fun Context.getThemeColor(attr: Int, defColor: Int = 0): Int {
    val typedValue = obtainStyledAttributes(intArrayOf(attr))
    val result = typedValue.getColor(0, defColor)
    typedValue.recycle()
    return result
}