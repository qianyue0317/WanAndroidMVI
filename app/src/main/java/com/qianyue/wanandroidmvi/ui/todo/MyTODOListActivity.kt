package com.qianyue.wanandroidmvi.ui.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ui.showContentOrEmpty
import com.qianyue.wanandroidmvi.ui.uiintent.MyTODOUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyTODOUiState
import com.qianyue.wanandroidmvi.viewmodel.MyTODOViewModel
import com.qianyue.wanandroidmvi.widgets.classicConfig
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener

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

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBottomDialog() {
        BottomSheetDialog(this, R.style.WanBottomSheetDialog).apply {
            setContentView(R.layout.bottom_sheet_add_todo)

            val inputTitle = findViewById<TextInputLayout>(R.id.til_title)!!
            val inputDesc = findViewById<TextInputLayout>(R.id.til_desc)!!

            val tvDate = findViewById<TextView>(R.id.tv_date_pick)!!
            tvDate.setSafeClickListener {
                DatePickerDialog(this@MyTODOListActivity).apply {
                    setOnDateSetListener { _, year, month, dayOfMonth ->
                        tvDate.text = "$year-${month + 1}-$dayOfMonth"
                    }
                }
                    .show()
            }

            findViewById<TextView>(R.id.tv_confirm)!!.setSafeClickListener {
                if (validInput(inputTitle, inputDesc)) {
                    vm.sendUiIntent(
                        MyTODOUiIntent.AddTodoItem(
                            inputTitle.editText!!.text.toString().trim(),
                            inputDesc.editText!!.text.toString().trim(),
                            tvDate.text.toString().trim()
                        )
                    )
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
                adapter.showContentOrEmpty(binding.emptyLayout)
            }

            is MyTODOUiState.OnAddSuccess -> {
                vm.sendUiIntent(MyTODOUiIntent.Refresh())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}