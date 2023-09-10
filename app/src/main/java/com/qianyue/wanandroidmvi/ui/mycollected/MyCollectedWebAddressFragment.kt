package com.qianyue.wanandroidmvi.ui.mycollected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ui.uiintent.MyCollectedIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyCollectedState
import com.qianyue.wanandroidmvi.viewmodel.MyCollectedViewModel
import com.qianyue.wanandroidmvi.widgets.classicConfig

/**
 * @author QianYue
 * @since 2023/9/9
 */
class MyCollectedWebAddressFragment : BaseFragment<MyCollectedViewModel>() {
    private var adapter: WebAddressAdapter? = null
    private var _binding: ListDataLayoutBinding? = null

    private val binding: ListDataLayoutBinding get() = _binding!!

    override fun lazyVM(): Lazy<MyCollectedViewModel> = viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListDataLayoutBinding.inflate(inflater, container, false)

        binding.emptyLayout.showProgressBar("正在加载")

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.refreshLayout.classicConfig(onRefresh = {
            vm.sendUiIntent(MyCollectedIntent.RefreshWebAddressData())
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this@MyCollectedWebAddressFragment.adapter = WebAddressAdapter()
            adapter = this@MyCollectedWebAddressFragment.adapter
        }
        adapter?.onUncollectItemListener = {
            vm.sendUiIntent(MyCollectedIntent.UncollectWebAddress(it.id))
        }
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is MyCollectedState.Init -> vm.sendUiIntent(MyCollectedIntent.RefreshWebAddressData())
            is MyCollectedState.OnRefreshWebAddressList -> {
                binding.refreshLayout.finishRefresh()
                if (state.list == null) {
                    binding.emptyLayout.showRefresh("加载失败，请重试") { }
                    return
                }
                if (state.list!!.isEmpty()) {
                    binding.emptyLayout.showNoData()
                    return
                }
                binding.emptyLayout.showContent()
                adapter?.submitList(state.list)
            }

            is MyCollectedState.UncollectResult -> {
                if (state.successful) {
                    adapter?.removeAt(state.position)
                    if (adapter?.items?.isEmpty() != false) {
                        binding.emptyLayout.showNoData()
                    }
                }
                else Toaster.showShort("操作失败 -- ${state.errorMsg}")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}