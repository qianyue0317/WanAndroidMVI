package com.qianyue.wanandroidmvi.model.bean

/**
 * 待办条目
 *
 * @since 2023/9/10
 * @author qy
 */
data class TodoItem(
    val completeDate: Long,
    val completeDateStr: String,
    val content: String,
    val date: Long,
    val dateStr: String,
    val id: Int,
    val priority: Int,
    val status: Int,
    val title: String,
    val type: Int,
    val userId: Int
)