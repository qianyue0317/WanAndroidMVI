package com.qianyue.wanandroidmvi

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.viewModelScope
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ActivityMainBinding
import com.qianyue.wanandroidmvi.ui.home.HomeFragment
import com.qianyue.wanandroidmvi.ui.mine.MineFragment
import com.qianyue.wanandroidmvi.ui.plaza.PlazaFragment
import com.qianyue.wanandroidmvi.ui.projectlist.ProjectFragment
import com.qianyue.wanandroidmvi.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author QianYue
 * @since 2023/8/15
 */
class MainActivity : BaseActivity<MainViewModel>() {

    private lateinit var binding: ActivityMainBinding

    private var _cacheFrag: Fragment? = null

    private val _fragTags = arrayOf("home_frag", "project_frag", "plaza_frag", "mine_frag")

    private var _quit = false

    override fun lazyVM(): Lazy<MainViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.setOnItemSelectedListener {
            supportActionBar?.title = it.title
            val transaction = supportFragmentManager.beginTransaction()
            _cacheFrag?.apply { transaction.hide(this) }
            when (it.itemId) {
                R.id.navigation_home -> {
                    val tempFrag = supportFragmentManager.findFragmentByTag(_fragTags[0])
                    _cacheFrag = tempFrag
                    tempFrag?.apply { transaction.show(this) }
                        ?: transaction.add(
                            R.id.fragment_container,
                            HomeFragment().apply { _cacheFrag = this },
                            _fragTags[0]
                        )
                }

                R.id.navigation_project -> {
                    val tempFrag = supportFragmentManager.findFragmentByTag(_fragTags[1])
                    _cacheFrag = tempFrag
                    tempFrag?.apply { transaction.show(this) }
                        ?: transaction.add(
                            R.id.fragment_container,
                            ProjectFragment().apply { _cacheFrag = this },
                            _fragTags[1]
                        )
                }

                R.id.navigation_plaza -> {
                    val tempFrag = supportFragmentManager.findFragmentByTag(_fragTags[2])
                    _cacheFrag = tempFrag
                    tempFrag?.apply { transaction.show(this) }
                        ?: transaction.add(
                            R.id.fragment_container,
                            PlazaFragment().apply { _cacheFrag = this },
                            _fragTags[2]
                        )
                }

                R.id.navigation_mine -> {
                    val tempFrag = supportFragmentManager.findFragmentByTag(_fragTags[3])
                    _cacheFrag = tempFrag
                    tempFrag?.apply { transaction.show(this) }
                        ?: transaction.add(
                            R.id.fragment_container,
                            MineFragment().apply { _cacheFrag = this },
                            _fragTags[3]
                        )
                }
            }
            transaction.commitAllowingStateLoss()
            true
        }

        navView.selectedItemId = R.id.navigation_home

        onBackPressedDispatcher.addCallback {
            vm.viewModelScope.launch {
                if (_quit) {
                    finish()
                    return@launch
                }
                _quit = true
                Toaster.showShort("再按一次退出应用")
                delay(1000)
                _quit = false
            }
        }
    }

    override suspend fun handleState(state: IUiState) {
    }
}