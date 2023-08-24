package com.qianyue.wanandroidmvi.model.bean

/**
 * @author QianYue
 * @since 2023/8/16
 */
data class BannerItem(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
)