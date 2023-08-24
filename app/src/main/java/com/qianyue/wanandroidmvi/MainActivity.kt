package com.qianyue.wanandroidmvi

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.databinding.ActivityMainBinding
import com.qianyue.wanandroidmvi.viewmodel.MainViewModel
import kotlinx.coroutines.launch

/**
 * @author QianYue
 * @since 2023/8/15
 */
class MainActivity : BaseActivity<MainViewModel>() {

    private lateinit var binding: ActivityMainBinding

    override fun lazyVM(): Lazy<MainViewModel> =
        lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_project,
                R.id.navigation_plaza,
                R.id.navigation_mine
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        vm.viewModelScope.launch {

        }
    }

}