package com.qianyue.wanandroidmvi.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentMineBinding
import com.qianyue.wanandroidmvi.ext.getThemeColor
import com.qianyue.wanandroidmvi.ext.observeBetter
import com.qianyue.wanandroidmvi.ui.login.LoginActivity
import com.qianyue.wanandroidmvi.ui.mycollected.MyCollectedActivity
import com.qianyue.wanandroidmvi.ui.myshared.MySharedActivity
import com.qianyue.wanandroidmvi.ui.setting.SettingActivity
import com.qianyue.wanandroidmvi.ui.todo.MyTODOListActivity
import com.qianyue.wanandroidmvi.ui.uiintent.MineUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MineUiState
import com.qianyue.wanandroidmvi.user.User
import com.qianyue.wanandroidmvi.viewmodel.MineViewModel
import com.qianyue.wanandroidmvi.widgets.RoundedDrawable
import com.qianyue.wanandroidmvi.widgets.circleOutline
import com.qianyue.wanandroidmvi.widgets.dp2px
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener

/**
 * 我的tab fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class MineFragment : BaseFragment<MineViewModel>() {

    private var _binding: FragmentMineBinding? = null

    private val binding get() = _binding!!

    override fun lazyVM(): Lazy<MineViewModel> = viewModels()

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is MineUiState.OnGetCoin -> {
                binding.tvCoinCount.text = "积分：${state.coinCount}"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.ivMineAvatar.circleOutline()

        binding.llMineContainer.background = RoundedDrawable().apply {
            radiusLT = 24f.dp2px()
            radiusRT = 24f.dp2px()
            color = requireContext().getThemeColor(android.R.attr.colorBackground)
        }

        initListener()
        initUserView()

        if (User.isLoginSuccess()) vm.sendUiIntent(MineUiIntent.GetUserCoin())

        User.userStateData.observeBetter(this.viewLifecycleOwner) { initUserView() }
        return root
    }

    private fun initListener() {
        binding.tvName.setSafeClickListener {
            if (!User.isLoginSuccess()) startActivity(
                Intent(
                    requireContext(),
                    LoginActivity::class.java
                )
            )
        }
        binding.llMyCollectItem.setSafeClickListener {
            if (User.isLoginSuccess()) {
                startActivity(Intent(requireContext(), MyCollectedActivity::class.java))
            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
        binding.llMyShareItem.setSafeClickListener {
            if (User.isLoginSuccess()) {
                startActivity(Intent(requireContext(), MySharedActivity::class.java))
            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
        binding.llTodoItem.setSafeClickListener {
            if (User.isLoginSuccess()) {
                startActivity(Intent(requireContext(), MyTODOListActivity::class.java))
            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
        binding.llSettingItem.setSafeClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }

    }

    private fun initUserView() {
        binding.tvName.text = if (User.isLoginSuccess()) User.userName else "登录/注册"
        binding.tvCoinCount.text = "积分：${User.coinCount}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}