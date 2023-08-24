package com.qianyue.wanandroidmvi.ui.plaza

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentPlazaBinding
import com.qianyue.wanandroidmvi.viewmodel.PlazaViewModel

/**
 * 广场tab fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class PlazaFragment : BaseFragment<PlazaViewModel>() {

    private var _binding: FragmentPlazaBinding? = null

    private val binding get() = _binding!!
    override fun lazyVM(): Lazy<PlazaViewModel> =
        lazy { ViewModelProvider(this)[PlazaViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlazaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun handleState(state: IUiState) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}