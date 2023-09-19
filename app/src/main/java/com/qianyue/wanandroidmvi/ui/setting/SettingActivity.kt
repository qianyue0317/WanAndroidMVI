package com.qianyue.wanandroidmvi.ui.setting

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ActivitySettingBinding
import com.qianyue.wanandroidmvi.ui.uiintent.SettingUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.SettingUiState
import com.qianyue.wanandroidmvi.user.User
import com.qianyue.wanandroidmvi.viewmodel.SettingViewModel
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener

/**
 * @author QianYue
 * @since 2023/9/19
 */
class SettingActivity : BaseActivity<SettingViewModel>() {
    private var _binding: ActivitySettingBinding? = null

    private val binding: ActivitySettingBinding get() = _binding!!

    override fun lazyVM(): Lazy<SettingViewModel> = viewModels()

    private var _progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "设置"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvLogout.setSafeClickListener {
            if (User.isLoginSuccess()) {
                AlertDialog.Builder(this).setTitle("提示").setMessage("确定要退出吗？")
                    .setPositiveButton("确定") { _, _ -> showProgressDialog();vm.sendUiIntent(SettingUiIntent.Logout()) }
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }.create().show()
            }
        }
    }

    private fun showProgressDialog() {
        _progressDialog = ProgressDialog.show(this, "正在请求...", "")
    }

    private fun dismissProgressDialog() {
        _progressDialog?.dismiss()
        _progressDialog = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is SettingUiState.OnLogout -> {
                dismissProgressDialog()
                Toaster.showShort("已退出登录")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}