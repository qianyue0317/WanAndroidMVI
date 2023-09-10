package com.qianyue.wanandroidmvi.ui.detailwebpage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.qianyue.wanandroidmvi.R
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
        fun startActivity(context: Context, url: String?, title: String?) {
            context.startActivity(Intent(context, DetailWebPageActivity::class.java).apply {
                putExtra("page_url", url ?: "")
                putExtra("page_title", title ?: "")
            })
        }
    }

    override fun lazyVM(): Lazy<DetailWebPageViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailWebPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url = intent.getStringExtra("page_url")
        val title = intent.getStringExtra("page_title")
        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
        binding.webView.webViewClient = object : WebViewClient() {}
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(url)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.detail_action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override suspend fun handleState(state: IUiState) {

    }
}