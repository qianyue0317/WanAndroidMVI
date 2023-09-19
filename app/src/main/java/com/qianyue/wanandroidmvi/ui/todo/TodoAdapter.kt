package com.qianyue.wanandroidmvi.ui.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qianyue.wanandroidmvi.databinding.ItemTodoBinding
import com.qianyue.wanandroidmvi.model.bean.TodoItem

/**
 * @author QianYue
 * @since 2023/9/19
 */
class TodoAdapter : BaseQuickAdapter<TodoItem, TodoAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: TodoItem?) {
        holder.binding.tvTitle.text = item?.title
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }
}