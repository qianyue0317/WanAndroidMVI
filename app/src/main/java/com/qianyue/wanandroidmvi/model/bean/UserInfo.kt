package com.qianyue.wanandroidmvi.model.bean


/**
 * @author QianYue
 * @since 2023/8/15
 */
data class UserInfo(var admin: Boolean = false,
                    var chapterTops: List<String> = listOf(),
                    var collectIds: MutableList<String> = mutableListOf(),
                    var email: String="",
                    var icon: String="",
                    var coinCount: Int = 0,
                    var id: String="",
                    var nickname: String="",
                    var publicName: String = "",
                    var password: String="",
                    var token: String="",
                    var type: Int =0,
                    var username: String="")
