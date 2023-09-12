package com.qianyue.wanandroidmvi.model.bean

/**
 * 热词
 *
 * @since 2023/9/11
 */
data class KeyWord(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)