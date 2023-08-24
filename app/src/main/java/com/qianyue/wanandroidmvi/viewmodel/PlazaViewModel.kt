package com.qianyue.wanandroidmvi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.ui.uiintent.PlazaUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.PlazaUiState

class PlazaViewModel : BaseViewModel<PlazaUiIntent, PlazaUiState>() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    override fun initState(): PlazaUiState = PlazaUiState()

    override fun processIntent(uiIntent: PlazaUiIntent) {

    }
}