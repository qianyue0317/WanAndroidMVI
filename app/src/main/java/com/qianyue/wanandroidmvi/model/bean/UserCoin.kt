package com.qianyue.wanandroidmvi.model.bean

/**
 * 获取用户积分信息
 *
 * @since 2023/9/17
 * @author qianyue
 */
data class UserCoin(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)