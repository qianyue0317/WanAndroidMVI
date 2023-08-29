package com.qianyue.wanandroidmvi.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

/**
 * @author QianYue
 * @since 2023/8/22
 */
object WanImageLoader {
    fun loadImage(
        context: Context,
        url: String?,
        iv: ImageView,
        radius: Int = 0,
        centerCrop: Boolean = false
    ) {
        Glide.with(context).asBitmap()
            .let { if (radius > 0) it.transform(RoundedCorners(radius)) else it }
            .let { if (centerCrop) it.transform(CenterCrop()) else it }.load(url).into(iv)
    }
}