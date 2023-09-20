package com.qianyue.wanandroidmvi.ui.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ext.dismissProgress
import com.qianyue.wanandroidmvi.ext.showAlertDialogSuspend
import com.qianyue.wanandroidmvi.ext.showProgress
import com.qianyue.wanandroidmvi.model.bean.TodoItem
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.showContentOrEmpty
import com.qianyue.wanandroidmvi.ui.uiintent.MyTODOUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyTODOUiState
import com.qianyue.wanandroidmvi.viewmodel.MyTODOViewModel
import com.qianyue.wanandroidmvi.widgets.circleOutline
import com.qianyue.wanandroidmvi.widgets.classicConfig
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

/**
 * @author QianYue
 * @since 2023/9/10
 */
class MyTODOListActivity : BaseActivity<MyTODOViewModel>() {
    private var _binding: ListDataLayoutBinding? = null

    private val binding: ListDataLayoutBinding get() = _binding!!

    private lateinit var adapter: TodoAdapter

    override fun lazyVM(): Lazy<MyTODOViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ListDataLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.apply {
            title = "我的待办"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.emptyLayout.showProgressBar()
        binding.refreshLayout.classicConfig(
            onRefresh = {
                vm.sendUiIntent(MyTODOUiIntent.Refresh())
            },
            onLoadMore = {
                vm.sendUiIntent(MyTODOUiIntent.LoadMore())
            }
        )
        adapter = TodoAdapter()

        adapter.onItemClick = {
            showBottomDialog(it)
        }

        adapter.onDeleteClick = {
            lifecycleScope.launch {
                val result = showAlertDialogSuspend("提示", "确定要删除吗？")
                if (result == true) {
                    showProgress()
                    vm.sendUiIntent(MyTODOUiIntent.Delete(it.id))
                }
            }
        }

        adapter.onFinishClick = {
            showProgress()
            vm.sendUiIntent(MyTODOUiIntent.Finish(it.id, it.status))
        }

        binding.recyclerView.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu_plus, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_plus -> {
                showBottomDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBottomDialog(
        todoItem: TodoItem? = null
    ) {
        BottomSheetDialog(this, R.style.WanBottomSheetDialog).apply {
            setContentView(R.layout.bottom_sheet_add_todo)

            var resultDate = todoItem?.dateStr
            var resultLevel = todoItem?.priority
            var resultType = todoItem?.type

            val inputTitle = findViewById<TextInputLayout>(R.id.til_title)!!
            val inputDesc = findViewById<TextInputLayout>(R.id.til_desc)!!
            inputTitle.editText!!.setText(todoItem?.title ?: "")
            inputDesc.editText!!.setText(todoItem?.content ?: "")

            val tvDate = findViewById<TextView>(R.id.tv_date_pick)!!.apply { text = resultDate ?: text }
            val tvLevel = findViewById<TextView>(R.id.tv_level)!!.apply {
                text = resultLevel?.let { MyTODOViewModel.PRIORITY[max(0, it - 1)] } ?: text
            }
            val tvType = findViewById<TextView>(R.id.tv_type)!!.apply {
                text = resultType?.let { MyTODOViewModel.TYPE_LIST[max(0, it - 1)] } ?: text
            }

            tvDate.circleOutline()
            tvLevel.circleOutline()
            tvType.circleOutline()

            tvDate.setSafeClickListener {
                DatePickerDialog(this@MyTODOListActivity).apply {
                    setOnDateSetListener { _, year, month, dayOfMonth ->
                        tvDate.text = "$year-${month + 1}-$dayOfMonth"
                        resultDate = tvDate.text.toString().trim()
                    }
                }.show()
            }

            tvLevel.setSafeClickListener {
                val items = MyTODOViewModel.PRIORITY
                AlertDialog.Builder(this@MyTODOListActivity)
                    .setItems(items) { _, index ->
                        resultLevel = index + 1
                        tvLevel.text = items[index]
                    }.show()
            }

            tvType.setSafeClickListener {
                val items = MyTODOViewModel.TYPE_LIST
                AlertDialog.Builder(this@MyTODOListActivity)
                    .setItems(items) { _, index ->
                        resultType = index + 1
                        tvType.text = items[index]
                    }.show()
            }

            findViewById<TextView>(R.id.tv_confirm)!!.setSafeClickListener {
                if (validInput(inputTitle, inputDesc)) {
                    showProgress()
                    if (todoItem != null) {
                        vm.sendUiIntent(
                            MyTODOUiIntent.UpdateTodoItem(
                                todoItem.id,
                                todoItem.status,
                                inputTitle.editText!!.text.toString().trim(),
                                inputDesc.editText!!.text.toString().trim(),
                                resultDate!!,
                                resultLevel!!,
                                resultType!!
                            )
                        )
                    } else {
                        vm.sendUiIntent(
                            MyTODOUiIntent.AddTodoItem(
                                inputTitle.editText!!.text.toString().trim(),
                                inputDesc.editText!!.text.toString().trim(),
                                resultDate,
                                resultLevel,
                                resultType
                            )
                        )
                    }
                    dismiss()
                }
            }
            show()
        }
    }


    private fun validInput(inputTitle: TextInputLayout, inputDesc: TextInputLayout): Boolean {
        if (TextUtils.isEmpty(inputTitle.editText!!.text.toString().trim())) {
            inputTitle.error = "标题不能为空"
            return false
        }
        if (TextUtils.isEmpty(inputDesc.editText!!.text.toString().trim())) {
            inputDesc.error = "详情不能为空"
            return false
        }
        return true
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is MyTODOUiState.Init -> vm.sendUiIntent(MyTODOUiIntent.Refresh())
            is MyTODOUiState.OnRefresh -> {
                adapter.submitList(state.list)
                binding.refreshLayout.finishRefresh()
                adapter.showContentOrEmpty(binding.emptyLayout)
            }

            is MyTODOUiState.OnLoadMore -> {
                adapter.safeAddAll(state.list)
                binding.refreshLayout.finishLoadMore()
                adapter.showContentOrEmpty(binding.emptyLayout)
            }

            is MyTODOUiState.OnAddResult -> {
                dismissProgress()
                if (state.successful) vm.sendUiIntent(MyTODOUiIntent.Refresh())
                else Toaster.showShort("添加失败--${state.msg}")
            }

            is MyTODOUiState.OnDeleteResult -> {
                dismissProgress()
                if (state.successful) vm.sendUiIntent(MyTODOUiIntent.Refresh())
                else Toaster.showShort("删除失败--${state.msg}")
            }

            is MyTODOUiState.OnUpdateResult -> {
                dismissProgress()
                if (state.successful) vm.sendUiIntent(MyTODOUiIntent.Refresh())
                else Toaster.showShort("更新失败--${state.errorMsg}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}