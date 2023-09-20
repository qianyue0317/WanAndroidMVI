package com.qianyue.wanandroidmvi.ui.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qianyue.wanandroidmvi.databinding.ItemTodoBinding
import com.qianyue.wanandroidmvi.model.bean.TodoItem
import com.qianyue.wanandroidmvi.viewmodel.MyTODOViewModel
import com.qianyue.wanandroidmvi.widgets.circleOutline
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener

/**
 * @author QianYue
 * @since 2023/9/19
 */
class TodoAdapter : BaseQuickAdapter<TodoItem, TodoAdapter.ViewHolder>() {

    var onItemClick: ((TodoItem) -> Unit)? = null

    var onDeleteClick: ((TodoItem) -> Unit)? = null

    var onFinishClick: ((TodoItem) -> Unit)? = null

    class ViewHolder(val binding: ItemTodoBinding, var cacheItem: TodoItem? = null) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: TodoItem?) {
        holder.binding.tvTitle.text = item?.title
        holder.binding.tvContent.text = item?.content
        holder.cacheItem = item
        holder.binding.ivFinished.visibility = if (item?.status == 1) View.VISIBLE else View.GONE
        holder.binding.tvFinish.text = if (item?.status == 1) "标记未完成" else "标记完成"
        holder.binding.tvType.text =
            MyTODOViewModel.TYPE_LIST.let { it[((item?.type ?: 1) - 1) % it.size] }
        holder.binding.tvLevel.text =
            MyTODOViewModel.PRIORITY.let { it[((item?.priority ?: 1) - 1) % it.size] }
        holder.binding.tvTodoDate.text = item?.dateStr
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.tvDelete.circleOutline()
        binding.tvFinish.circleOutline()
        val viewHolder = ViewHolder(binding)

        binding.tvDelete.setSafeClickListener {
            viewHolder.cacheItem ?: return@setSafeClickListener
            onDeleteClick?.invoke(viewHolder.cacheItem!!)
        }

        binding.tvFinish.setSafeClickListener {
            viewHolder.cacheItem ?: return@setSafeClickListener
            onFinishClick?.invoke(viewHolder.cacheItem!!)
        }

        binding.root.setSafeClickListener {
            viewHolder.cacheItem ?: return@setSafeClickListener
            onItemClick?.invoke(viewHolder.cacheItem!!)
        }
        return viewHolder
    }
}