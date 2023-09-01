package com.qianyue.wanandroidmvi.ext

import android.content.Context
import android.widget.ImageView
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