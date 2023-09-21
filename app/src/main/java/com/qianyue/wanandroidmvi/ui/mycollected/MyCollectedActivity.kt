package com.qianyue.wanandroidmvi.ui.mycollected

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ActivityMyCollectedBinding
import com.qianyue.wanandroidmvi.viewmodel.MyCollectedViewModel

/**
 * 我的收藏页面
 *
 * @author QianYue
 * @since 2023/8/31
 */
class MyCollectedActivity: BaseActivity<MyCollectedViewModel>() {

    private var fragments: MutableList<Fragment> = mutableListOf()

    private var _binding: ActivityMyCollectedBinding? = null

    private val binding: ActivityMyCollectedBinding get() = _binding!!

    init {
        fragments.add(MyCollectedArticleFragment())
        fragments.add(MyCollectedWebAddressFragment())
    }

    override fun lazyVM(): Lazy<MyCollectedViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyCollectedBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.apply {
            title = "我的收藏"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int = 2

            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getPageTitle(position: Int): CharSequence = if (position == 0) "文章" else "网站"
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    override suspend fun handleState(state: IUiState) {
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}