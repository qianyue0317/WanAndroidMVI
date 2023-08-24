package com.qianyue.wanandroidmvi.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentMineBinding
import com.qianyue.wanandroidmvi.viewmodel.MineViewModel

/**
 * 我的tab fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class MineFragment : BaseFragment<MineViewModel>() {

    private var _binding: FragmentMineBinding? = null

    private val binding get() = _binding!!

    override fun lazyVM(): Lazy<MineViewModel> =
        lazy { ViewModelProvider(this)[MineViewModel::class.java] }

    override fun handleState(state: IUiState) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}