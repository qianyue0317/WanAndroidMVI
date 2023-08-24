package com.qianyue.wanandroidmvi.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * @author QianYue
 * @since 2023/8/22
 */
object WanImageLoader {
    fun loadImage(context: Context, url: String?, iv: ImageView) {
        Glide.with(context).asBitmap().load(url).into(iv)
    }
}