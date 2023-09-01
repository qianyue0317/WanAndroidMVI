package com.qianyue.wanandroidmvi

import android.app.Application
import android.content.Context
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.user.User
import com.tencent.mmkv.MMKV
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback

/**
 * 全局App类
 *
 * @author qy
 * @since 2023-08-14
 */
class WanApplication: Application() {
    companion object {
        lateinit var instance: WanApplication
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        // 初始化腾讯 WebView start
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map);
        QbSdk.initX5Environment(this, object : PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {

            }

        })
        // 初始化腾讯 WebView end

        // 初始化toast
        Toaster.init(this)

        // 加载缓存user
        User.loadCacheUser()
    }
}