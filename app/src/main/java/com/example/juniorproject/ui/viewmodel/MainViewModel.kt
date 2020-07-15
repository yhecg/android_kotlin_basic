package com.example.juniorproject.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * View 또는 액티비티 Context에 대한 레퍼런스를 가지면 안됩니다.
 * View는 ViewModel의 reference를 가지지만 ViewModel은 View에 대한 정보가 전혀 없어야 합니다.
 * (ViewModel이 View의 레퍼런스를 가진다면 lifecycle 에 따른 메모리릭이 발생할 수 있는데
 * 그 이유는 ViewModel이 destroy 외의 라이프사이클에서는 메모리에서 해제되지 않기 때문입니다.)
 */
class MainViewModel : ViewModel() {

    private val TAG = MainViewModel::class.java.simpleName

    // 액티비티 띄우기
    var showAct = MutableLiveData<Int>()
    fun callShowActivity(type:Int){
        showAct.value = type
    }


}