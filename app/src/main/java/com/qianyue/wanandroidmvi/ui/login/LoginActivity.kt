package com.qianyue.wanandroidmvi.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ActivityLoginBinding
import com.qianyue.wanandroidmvi.ext.dismissProgress
import com.qianyue.wanandroidmvi.ext.showProgress
import com.qianyue.wanandroidmvi.ui.uiintent.LoginUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.LoginUiState
import com.qianyue.wanandroidmvi.viewmodel.LoginViewModel
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener

/**
 * @author QianYue
 * @since 2023/8/30
 */
class LoginActivity: BaseActivity<LoginViewModel>() {
    private var _binding : ActivityLoginBinding? = null

    private val binding get() = _binding!!

    override fun lazyVM(): Lazy<LoginViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "登录/注册"
        }
        initView()
    }

    private fun initView() {
        binding.tvTip.setSafeClickListener {
            vm.sendUiIntent(LoginUiIntent.ChangeModeIntent())
        }
        binding.btnLoginRegister.setSafeClickListener {
            if (TextUtils.isEmpty(binding.etName.text.toString().trim())) {
                binding.tilName.error = "用户名不能为空"
                return@setSafeClickListener
            }
            binding.tilName.error = null
            if (TextUtils.isEmpty(binding.etPassword.text.toString().trim())) {
                binding.tilPassword.error = "密码不能为空"
                return@setSafeClickListener
            }
            binding.tilPassword.error = null
            if (!vm.isLogin && TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim())) {
                binding.tilConfirmPassword.error = "确认密码不能为空"
                return@setSafeClickListener
            }
            binding.tilConfirmPassword.error = null
            vm.sendUiIntent(LoginUiIntent.Submit(binding.etName.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.text.toString()))
        }
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is LoginUiState.InitState, is LoginUiState.ModeChangeState -> {
                val isLogin = if (state is LoginUiState.InitState) state.isLogin else (state as LoginUiState.ModeChangeState).isLogin
                binding.tvTip.text = if (isLogin) "没有账号？去注册" else "有账号了？去登录"
                binding.tilConfirmPassword.visibility = if (isLogin) View.GONE else View.VISIBLE
                binding.tvTitle.text = if (isLogin) "登录" else "注册"
                binding.btnLoginRegister.text = if (isLogin) "登录" else "注册"
            }
            is LoginUiState.ShowLoading -> {
                showProgress()
            }
            is LoginUiState.LoginResult -> {
                dismissProgress()
                if (!state.success) {
                    Toaster.showShort("${if (vm.isLogin) "登录" else "注册"}失败 -- ${state.msg}")
                } else finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}