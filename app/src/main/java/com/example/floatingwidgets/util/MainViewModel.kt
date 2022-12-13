package com.example.floatingwidgets.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object MainViewModel: ViewModel() {

    //悬浮窗口创建 移除
//    val isShowFloatingWindow = MutableLiveData<Boolean>()

    //判断加载哪个悬浮窗口的标识变量
    val whichFloatingWindow = MutableLiveData<Int>()

}