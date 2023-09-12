package com.qianyue.wanandroidmvi.ui.todo

import android.os.Bundle
import androidx.activity.viewModels
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ui.uiintent.MyTODOUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyTODOUiState
import com.qianyue.wanandroidmvi.viewmodel.MyTODOViewModel

/**
 * @author QianYue
 * @since 2023/9/10
 */
class MyTODOListActivity: BaseActivity<MyTODOViewModel>() {
    private var _binding: ListDataLayoutBinding? = null

    private val binding: ListDataLayoutBinding = _binding!!

    override fun lazyVM(): Lazy<MyTODOViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ListDataLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is MyTODOUiState.Init -> vm.sendUiIntent(MyTODOUiIntent.Refresh())
            is MyTODOUiState.OnRefresh -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}