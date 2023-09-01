package com.qianyue.wanandroidmvi.ext

import androidx.arch.core.internal.SafeIterableMap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.qianyue.wanandroidmvi.utils.WanLog

/**
 * @author QianYue
 * @since 2023/9/1
 */

private class ObserverWrapper<T>(val observer: Observer<T>, var enableInner: Boolean): Observer<T> {

    override fun onChanged(value: T) {
        if (enableInner) observer.onChanged(value)
    }
}

/**
 * 可以有效防止数据倒灌的liveData观察
 *
 * 要防止两种情况下的倒灌
 * 1. 假如当前owner的状态是resume的，此时在observe方法中，内部的LifecycleBoundObserver需要把状态计算到resume，
 * 这个过程中会调用dispatchChange方法。换句话说：在observe方法中会触发一次observer的onChange，
 * 所以使用上边的ObserverWrapper包裹一层，并用一个变量控制是否调用原始observer，在调用observe方法之后将变量置为true
 *
 * 2. 假如当前owner的状态是小于等于create的，在observe方法中不会进行状态计算。
 * 但是一旦owner的状态变成resume之后，由于注册的observer的mLastVersion还没有更新，所以还是会将之前的value灌入observer，
 * 所以要通过反射的手段将observer的mLastVersion更新成LiveData最新的version值
 */
fun <T> LiveData<T>.observeBetter(owner: LifecycleOwner, observer: Observer<T>) {
    val wrapper = ObserverWrapper(observer, false)
    observe(owner, wrapper)
    kotlin.runCatching {
        val declaredField = this.javaClass.superclass.getDeclaredField("mObservers")
        val accessible = declaredField.isAccessible
        declaredField.isAccessible = true
        val observers = declaredField.get(this) as SafeIterableMap<*, *>?
        val observerLifecycleWrapper = observers?.findLast {
            it.key == wrapper
        }?.value
        val observerWrapperClz =
            observerLifecycleWrapper?.javaClass?.superclass ?: return@runCatching
        val lastVersionField = observerWrapperClz.getDeclaredField("mLastVersion")
        val versionAccessible = lastVersionField.isAccessible
        lastVersionField.isAccessible = true

        val liveDataVersion: Int? = this@observeBetter.javaClass.superclass.getDeclaredField("mVersion").let {
            val liveDataVersionAccessible = it.isAccessible
            it.isAccessible = true
            val result = it.get(this@observeBetter)
            it.isAccessible = liveDataVersionAccessible
            result as Int?
        }

        lastVersionField.set(observerLifecycleWrapper, liveDataVersion)

        lastVersionField.isAccessible = versionAccessible
        declaredField.isAccessible = accessible

        WanLog.d("observeBetter", "set lastVersion success")
    }.onFailure {
        WanLog.e(msg = "better: ${it.message}")
    }
    wrapper.enableInner = true
}