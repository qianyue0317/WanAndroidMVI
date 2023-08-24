package com.qianyue.wanandroidmvi.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author qy
 * @since 2023-08-14
 */
abstract class BaseActivity<VM : BaseViewModel<*, *>> : AppCompatActivity() {

    val vm: VM by lazyVM()

    abstract fun lazyVM(): Lazy<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}