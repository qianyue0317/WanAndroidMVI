package com.qianyue.wanandroidmvi.ui.detailwebpage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ActivityDetailWebPageBinding
import com.qianyue.wanandroidmvi.viewmodel.DetailWebPageViewModel
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebViewClient

/**
 * @author QianYue
 * @since 2023/8/23
 */
class DetailWebPageActivity : BaseActivity<DetailWebPageViewModel>() {

    var url: String? = null

    companion object {
        fun startActivity(context: Context, url: String?) {
            context.startActivity(Intent(context, DetailWebPageActivity::class.java).apply {
                putExtra("page_url", url?:"")
            })
        }
    }

    override fun lazyVM(): Lazy<DetailWebPageViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailWebPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url = intent.getStringExtra("page_url")
        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }

        binding.webView.webViewClient = object : WebViewClient() {}
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)
    }

    override suspend fun handleState(state: IUiState) {

    }
}