package com.qianyue.wanandroidmvi.base

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qianyue.wanandroidmvi.ext.dismissProgress
import com.qianyue.wanandroidmvi.utils.WanLog
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

/**
 * @author qy
 * @since 2023-08-14
 */
abstract class BaseActivity<VM : BaseViewModel<*, *>> : AppCompatActivity() {

    val vm: VM by lazyVM()

    var progressDialog: Dialog? = null

    abstract fun lazyVM(): Lazy<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.uiStateFlow.collectIndexed { _, value ->
                    handleState(value)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    abstract suspend fun handleState(state: IUiState)

    override fun onDestroy() {
        super.onDestroy()
        dismissProgress()
        vm.onDestroy()
    }

}