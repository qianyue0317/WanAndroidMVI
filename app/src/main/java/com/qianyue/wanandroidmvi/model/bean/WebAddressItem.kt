package com.qianyue.wanandroidmvi.model.bean

/**
 * 网站地址条目
 *
 * @author qianyue
 * @since 2023/9/9
 */
data class WebAddressItem(
    val desc: String,
    val icon: String,
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val userId: Int,
    val visible: Int
)